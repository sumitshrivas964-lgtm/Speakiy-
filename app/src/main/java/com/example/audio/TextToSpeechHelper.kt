package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID

class TextToSpeechHelper(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentlyPlayingId = MutableStateFlow<String?>(null)
    val currentlyPlayingId: StateFlow<String?> = _currentlyPlayingId.asStateFlow()

    var onSpeakingDone: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setSpeechRate(0.92f) // Slightly slower for language learners
            tts?.setPitch(1.0f)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlyPlayingId.value = null
                    onSpeakingDone?.invoke()
                }

                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlyPlayingId.value = null
                }
            })
        } else {
            Log.e("TextToSpeechHelper", "TTS Initialization failed with status $status")
        }
    }

    fun speak(text: String, locale: Locale, turnId: String? = null, speechRate: Float = 0.92f) {
        if (!isInitialized || tts == null) {
            Log.w("TextToSpeechHelper", "TTS not ready yet")
            return
        }

        stop()

        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English if exact dialect is not on device
                tts?.setLanguage(Locale.ENGLISH)
            }
            tts?.setSpeechRate(speechRate)
            _currentlyPlayingId.value = turnId
            val utteranceId = turnId ?: UUID.randomUUID().toString()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (e: Exception) {
            Log.e("TextToSpeechHelper", "Error speaking text", e)
            _isSpeaking.value = false
            _currentlyPlayingId.value = null
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.w("TextToSpeechHelper", "Error stopping TTS", e)
        }
        _isSpeaking.value = false
        _currentlyPlayingId.value = null
    }

    fun shutdown() {
        stop()
        try {
            tts?.shutdown()
        } catch (e: Exception) {
            Log.w("TextToSpeechHelper", "Error shutting down TTS", e)
        }
        tts = null
        isInitialized = false
    }
}
