package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeakiyDao {
    @Query("SELECT * FROM conversation_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<ConversationSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ConversationSessionEntity)

    @Query("SELECT * FROM conversation_turns WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getTurnsForSession(sessionId: String): Flow<List<ConversationTurnEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurn(turn: ConversationTurnEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedRule(rule: SavedRuleEntity)

    @Query("SELECT * FROM saved_rules ORDER BY savedAt DESC")
    fun getAllSavedRules(): Flow<List<SavedRuleEntity>>

    @Query("DELETE FROM saved_rules WHERE id = :ruleId")
    suspend fun deleteSavedRule(ruleId: Int)

    @Query("SELECT COUNT(*) FROM conversation_turns WHERE isUser = 1")
    suspend fun getUserTurnCount(): Int

    @Query("SELECT AVG(fluencyScore) FROM conversation_turns WHERE isUser = 1 AND fluencyScore > 0")
    suspend fun getAverageFluency(): Double?
}
