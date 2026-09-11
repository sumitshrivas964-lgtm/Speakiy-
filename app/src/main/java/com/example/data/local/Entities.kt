package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation_sessions")
data class ConversationSessionEntity(
    @PrimaryKey val id: String,
    val languageCode: String,
    val topic: String,
    val timestamp: Long,
    val totalTurns: Int,
    val avgFluency: Int
)

@Entity(tableName = "conversation_turns")
data class ConversationTurnEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val isUser: Boolean,
    val text: String,
    val translation: String,
    val timestamp: Long,
    val fluencyScore: Int,
    val accuracyScore: Int,
    val correctedText: String,
    val explanation: String,
    val nativeAlternative: String,
    val ruleCategory: String,
    val vocabularyTip: String
)

@Entity(tableName = "saved_rules")
data class SavedRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageCode: String,
    val originalText: String,
    val correctedText: String,
    val ruleCategory: String,
    val explanation: String,
    val nativeAlternative: String,
    val savedAt: Long = System.currentTimeMillis()
)
