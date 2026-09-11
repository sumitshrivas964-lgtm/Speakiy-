package com.example.data.model

enum class VerificationTier(val displayName: String, val badgeColor: Long) {
    UNVERIFIED("Unverified Explorer", 0xFF64748B),
    EMAIL_VERIFIED("Email Confirmed", 0xFF0284C7),
    PHONE_VERIFIED("Phone Verified", 0xFF0D9488),
    FULLY_VERIFIED_NATIVE("Verified Native Speaker", 0xFF10B981)
}

data class UserProfile(
    val name: String = "Sumit Shrivas",
    val email: String = "sumitshrivas044@gmail.com",
    val phoneNumber: String = "+1 555-019-2834",
    val nativeLanguageCode: String = "en",
    val targetLanguageCode: String = "es",
    val fluencyLevel: String = "Intermediate B1",
    val bio: String = "Passionate language learner practicing fluent pronunciation and grammar feedback.",
    val isEmailVerified: Boolean = true,
    val isPhoneVerified: Boolean = true,
    val isVoiceBioVerified: Boolean = true,
    val voiceBioPrompt: String = "Spoke 20s native English greeting and idiom sample.",
    val developerCredit: String = "Sumit Shrivas",
    val developerEmail: String = "sumitshrivas044@gmail.com",
    val playStoreListingDeveloper: String = "Developed & Published by Sumit Shrivas on Google Play Store",
    val totalSpokenMinutes: Int = 54,
    val overallFluencyAverage: Int = 89,
    val totalCorrectionsReviewed: Int = 36
) {
    val completenessScore: Int get() {
        var score = 0
        if (name.isNotBlank()) score += 15
        if (bio.isNotBlank()) score += 15
        if (isEmailVerified) score += 20
        if (isPhoneVerified) score += 20
        if (isVoiceBioVerified) score += 20
        if (nativeLanguageCode.isNotBlank() && targetLanguageCode.isNotBlank()) score += 10
        return score.coerceAtMost(100)
    }

    val trustScore: Int get() {
        var score = 40
        if (isEmailVerified) score += 15
        if (isPhoneVerified) score += 20
        if (isVoiceBioVerified) score += 25
        return score.coerceAtMost(100)
    }

    val verificationTier: VerificationTier get() {
        return when {
            isEmailVerified && isPhoneVerified && isVoiceBioVerified -> VerificationTier.FULLY_VERIFIED_NATIVE
            isPhoneVerified -> VerificationTier.PHONE_VERIFIED
            isEmailVerified -> VerificationTier.EMAIL_VERIFIED
            else -> VerificationTier.UNVERIFIED
        }
    }

    val isVerified: Boolean get() = verificationTier != VerificationTier.UNVERIFIED
}
