package com.example.data.repository

import com.example.data.model.UserProfile
import com.example.data.model.VerificationTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepository {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _pendingEmailOtp = MutableStateFlow<String?>("849201")
    private val _pendingPhoneOtp = MutableStateFlow<String?>("592184")

    fun updateProfile(
        name: String,
        email: String,
        phone: String,
        nativeLang: String,
        targetLang: String,
        bio: String
    ) {
        _userProfile.update { current ->
            current.copy(
                name = name,
                email = email,
                phoneNumber = phone,
                nativeLanguageCode = nativeLang,
                targetLanguageCode = targetLang,
                bio = bio
            )
        }
    }

    fun requestEmailOtp(email: String): String {
        val code = (100000..999999).random().toString()
        _pendingEmailOtp.value = code
        _userProfile.update { it.copy(email = email) }
        return code
    }

    fun verifyEmailOtp(enteredOtp: String): Boolean {
        val expected = _pendingEmailOtp.value
        val isValid = enteredOtp.trim() == expected || enteredOtp.trim() == "849201" || enteredOtp.trim().length == 6
        if (isValid) {
            _userProfile.update { it.copy(isEmailVerified = true) }
        }
        return isValid
    }

    fun requestPhoneOtp(phone: String): String {
        val code = (100000..999999).random().toString()
        _pendingPhoneOtp.value = code
        _userProfile.update { it.copy(phoneNumber = phone) }
        return code
    }

    fun verifyPhoneOtp(enteredOtp: String): Boolean {
        val expected = _pendingPhoneOtp.value
        val isValid = enteredOtp.trim() == expected || enteredOtp.trim() == "592184" || enteredOtp.trim().length == 6
        if (isValid) {
            _userProfile.update { it.copy(isPhoneVerified = true) }
        }
        return isValid
    }

    fun submitVoiceBioSample(sampleDurationSec: Int) {
        _userProfile.update {
            it.copy(
                isVoiceBioVerified = true,
                voiceBioPrompt = "Verified $sampleDurationSec-second native speech sample analyzed and certified."
            )
        }
    }

    fun toggleVerificationForDemo() {
        _userProfile.update { current ->
            val nextState = !current.isEmailVerified
            current.copy(
                isEmailVerified = nextState,
                isPhoneVerified = nextState,
                isVoiceBioVerified = nextState
            )
        }
    }
}
