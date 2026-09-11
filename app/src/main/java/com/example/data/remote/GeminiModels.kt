package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float = 0.7f,
    val responseMimeType: String? = "application/json"
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class StructuredAiTurn(
    @Json(name = "reply") val reply: String = "",
    @Json(name = "replyEnglish") val replyEnglish: String? = null,
    @Json(name = "fluencyScore") val fluencyScore: Int = 85,
    @Json(name = "accuracyScore") val accuracyScore: Int = 85,
    @Json(name = "correctedSentence") val correctedSentence: String? = null,
    @Json(name = "explanation") val explanation: String? = null,
    @Json(name = "nativeAlternative") val nativeAlternative: String? = null,
    @Json(name = "ruleCategory") val ruleCategory: String? = null,
    @Json(name = "vocabularyTip") val vocabularyTip: String? = null
)
