package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SpeechRecognizerHelper
import com.example.audio.TextToSpeechHelper
import com.example.data.local.ConversationSessionEntity
import com.example.data.local.ConversationTurnEntity
import com.example.data.local.SavedRuleEntity
import com.example.data.local.SpeakiyDatabase
import com.example.data.model.ConversationTurn
import com.example.data.model.GrammarFeedback
import com.example.data.model.Language
import com.example.data.model.NativeSpeaker
import com.example.data.model.UserProfile
import com.example.data.remote.GeminiRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class SpeakiyScreen {
    CONVERSATION,
    NATIVE_MATCH,
    VERIFY_ACCOUNT,
    SAVED_RULES,
    PROFILE
}

data class UiState(
    val currentScreen: SpeakiyScreen = SpeakiyScreen.CONVERSATION,
    val selectedLanguage: Language = Language.findByCode("es"),
    val selectedTopic: String = "Comida típica",
    val turns: List<ConversationTurn> = emptyList(),
    val isLoadingAi: Boolean = false,
    val isRecordingVoiceBio: Boolean = false,
    val voiceBioSeconds: Int = 0,
    val selectedSpeakerForDetails: NativeSpeaker? = null,
    val activeFeedback: GrammarFeedback? = null,
    val showLanguagePicker: Boolean = false,
    val feedbackBannerText: String? = null,
    val emailOtpInput: String = "",
    val phoneOtpInput: String = "",
    val otpSentMessage: String? = null,
    val simulatedEmailOtp: String = "849201",
    val simulatedPhoneOtp: String = "592184"
)

class SpeakiyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SpeakiyDatabase.getDatabase(application)
    private val dao = db.speakiyDao()
    private val geminiRepo = GeminiRepository()
    private val userRepo = UserRepository()

    val speechRecognizer = SpeechRecognizerHelper(application)
    val textToSpeech = TextToSpeechHelper(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val userProfile: StateFlow<UserProfile> = userRepo.userProfile
    val savedRules: StateFlow<List<SavedRuleEntity>> = dao.getAllSavedRules()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var currentSessionId: String = UUID.randomUUID().toString()

    init {
        // Setup speech callback
        speechRecognizer.onSpeechFinalResult = { spokenText ->
            handleUserSpokenInput(spokenText)
        }

        // Initialize with default language greeting
        resetConversationForLanguage(Language.findByCode("es"))
    }

    fun setScreen(screen: SpeakiyScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectLanguage(lang: Language) {
        _uiState.update {
            it.copy(
                selectedLanguage = lang,
                selectedTopic = lang.starterTopics.firstOrNull() ?: "General Conversation",
                showLanguagePicker = false
            )
        }
        resetConversationForLanguage(lang)
    }

    fun selectTopic(topic: String) {
        _uiState.update { it.copy(selectedTopic = topic) }
        resetConversationForLanguage(_uiState.value.selectedLanguage, topic)
    }

    fun toggleLanguagePicker(show: Boolean) {
        _uiState.update { it.copy(showLanguagePicker = show) }
    }

    private fun resetConversationForLanguage(
        lang: Language,
        topic: String = lang.starterTopics.firstOrNull() ?: "General"
    ) {
        currentSessionId = UUID.randomUUID().toString()
        val initialGreetingTurn = ConversationTurn(
            isUser = false,
            text = lang.greeting,
            languageCode = lang.code
        )
        _uiState.update {
            it.copy(
                turns = listOf(initialGreetingTurn),
                selectedTopic = topic,
                activeFeedback = null
            )
        }
    }

    fun startListening() {
        val lang = _uiState.value.selectedLanguage
        speechRecognizer.startListening(lang.toLocale())
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun handleUserSpokenInput(text: String) {
        if (text.isBlank()) return

        val lang = _uiState.value.selectedLanguage
        val topic = _uiState.value.selectedTopic

        val userTurn = ConversationTurn(
            isUser = true,
            text = text,
            languageCode = lang.code
        )

        _uiState.update { current ->
            current.copy(
                turns = current.turns + userTurn,
                isLoadingAi = true
            )
        }

        viewModelScope.launch {
            try {
                val (aiReply, feedback) = geminiRepo.converseAndFeedback(
                    userUtterance = text,
                    history = _uiState.value.turns,
                    language = lang,
                    topic = topic
                )

                val updatedUserTurn = userTurn.copy(feedback = feedback)
                val aiTurn = ConversationTurn(
                    isUser = false,
                    text = aiReply,
                    languageCode = lang.code
                )

                _uiState.update { current ->
                    val updatedTurns = current.turns.map { if (it.id == userTurn.id) updatedUserTurn else it } + aiTurn
                    current.copy(
                        turns = updatedTurns,
                        isLoadingAi = false,
                        activeFeedback = feedback,
                        feedbackBannerText = "Feedback: ${feedback.ruleCategory} • Fluency ${feedback.fluencyScore}%"
                    )
                }

                // Automatically speak AI reply
                textToSpeech.speak(aiReply, lang.toLocale(), aiTurn.id)

                // Persist session and turns into local Room database
                persistSession(lang, topic, listOf(updatedUserTurn, aiTurn))
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingAi = false) }
            }
        }
    }

    fun speakTurn(turn: ConversationTurn) {
        val lang = Language.findByCode(turn.languageCode)
        textToSpeech.speak(turn.text, lang.toLocale(), turn.id)
    }

    fun stopTts() {
        textToSpeech.stop()
    }

    fun viewFeedback(feedback: GrammarFeedback) {
        _uiState.update { it.copy(activeFeedback = feedback) }
    }

    fun dismissFeedback() {
        _uiState.update { it.copy(activeFeedback = null) }
    }

    fun saveRuleToReview(feedback: GrammarFeedback) {
        viewModelScope.launch {
            dao.insertSavedRule(
                SavedRuleEntity(
                    languageCode = _uiState.value.selectedLanguage.code,
                    originalText = feedback.originalText,
                    correctedText = feedback.correctedText,
                    ruleCategory = feedback.ruleCategory,
                    explanation = feedback.explanation,
                    nativeAlternative = feedback.nativeAlternative
                )
            )
            _uiState.update { it.copy(feedbackBannerText = "Saved to Grammar Review!") }
        }
    }

    fun deleteSavedRule(ruleId: Int) {
        viewModelScope.launch {
            dao.deleteSavedRule(ruleId)
        }
    }

    // Account Verification Methods
    fun requestEmailOtp(email: String) {
        val otp = userRepo.requestEmailOtp(email)
        _uiState.update {
            it.copy(
                simulatedEmailOtp = otp,
                otpSentMessage = "Verification OTP sent to $email! (Use: $otp)"
            )
        }
    }

    fun updateEmailOtpInput(input: String) {
        _uiState.update { it.copy(emailOtpInput = input) }
    }

    fun verifyEmail() {
        val input = _uiState.value.emailOtpInput
        val verified = userRepo.verifyEmailOtp(input)
        if (verified) {
            _uiState.update {
                it.copy(
                    emailOtpInput = "",
                    otpSentMessage = "✓ Email successfully verified! Trust score increased."
                )
            }
        } else {
            _uiState.update { it.copy(otpSentMessage = "Invalid OTP code. Please enter 6-digit code.") }
        }
    }

    fun requestPhoneOtp(phone: String) {
        val otp = userRepo.requestPhoneOtp(phone)
        _uiState.update {
            it.copy(
                simulatedPhoneOtp = otp,
                otpSentMessage = "SMS OTP sent to $phone! (Use: $otp)"
            )
        }
    }

    fun updatePhoneOtpInput(input: String) {
        _uiState.update { it.copy(phoneOtpInput = input) }
    }

    fun verifyPhone() {
        val input = _uiState.value.phoneOtpInput
        val verified = userRepo.verifyPhoneOtp(input)
        if (verified) {
            _uiState.update {
                it.copy(
                    phoneOtpInput = "",
                    otpSentMessage = "✓ Phone number verified! Unlocked higher native match priority."
                )
            }
        } else {
            _uiState.update { it.copy(otpSentMessage = "Invalid SMS code. Please try again.") }
        }
    }

    fun completeVoiceBioVerification() {
        userRepo.submitVoiceBioSample(sampleDurationSec = 20)
        _uiState.update {
            it.copy(
                otpSentMessage = "✓ Voice sample authenticated! Awarded 'Verified Native Speaker' Badge."
            )
        }
    }

    fun toggleFullVerificationDemo() {
        userRepo.toggleVerificationForDemo()
    }

    fun selectSpeaker(speaker: NativeSpeaker?) {
        _uiState.update { it.copy(selectedSpeakerForDetails = speaker) }
    }

    fun startConversationWithSpeaker(speaker: NativeSpeaker) {
        val lang = Language.findByCode(speaker.nativeLanguageCode)
        selectLanguage(lang)
        _uiState.update {
            it.copy(
                currentScreen = SpeakiyScreen.CONVERSATION,
                selectedSpeakerForDetails = null
            )
        }
        textToSpeech.speak(speaker.voiceIntroText, lang.toLocale())
    }

    private suspend fun persistSession(
        language: Language,
        topic: String,
        newTurns: List<ConversationTurn>
    ) {
        val sessionEntity = ConversationSessionEntity(
            id = currentSessionId,
            languageCode = language.code,
            topic = topic,
            timestamp = System.currentTimeMillis(),
            totalTurns = _uiState.value.turns.size,
            avgFluency = 88
        )
        dao.insertSession(sessionEntity)

        for (turn in newTurns) {
            val turnEntity = ConversationTurnEntity(
                id = turn.id,
                sessionId = currentSessionId,
                isUser = turn.isUser,
                text = turn.text,
                translation = turn.translation,
                timestamp = turn.timestamp,
                fluencyScore = turn.feedback?.fluencyScore ?: 0,
                accuracyScore = turn.feedback?.accuracyScore ?: 0,
                correctedText = turn.feedback?.correctedText ?: "",
                explanation = turn.feedback?.explanation ?: "",
                nativeAlternative = turn.feedback?.nativeAlternative ?: "",
                ruleCategory = turn.feedback?.ruleCategory ?: "",
                vocabularyTip = turn.feedback?.vocabularyTip ?: ""
            )
            dao.insertTurn(turnEntity)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
        textToSpeech.shutdown()
    }
}
