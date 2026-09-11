package com.example.data.model

import java.util.UUID

enum class AudioPlaybackState {
    IDLE,
    LOADING,
    PLAYING
}

data class ConversationTurn(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val translation: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val languageCode: String,
    val feedback: GrammarFeedback? = null,
    val playbackState: AudioPlaybackState = AudioPlaybackState.IDLE
)
