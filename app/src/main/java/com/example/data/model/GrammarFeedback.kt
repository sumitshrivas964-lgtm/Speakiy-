package com.example.data.model

data class GrammarFeedback(
    val originalText: String,
    val correctedText: String,
    val fluencyScore: Int,
    val accuracyScore: Int,
    val explanation: String,
    val nativeAlternative: String,
    val ruleCategory: String,
    val vocabularyTip: String = ""
) {
    val isPerfect: Boolean get() = fluencyScore >= 90 && originalText.trim().equals(correctedText.trim(), ignoreCase = true)
}
