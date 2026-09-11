package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ConversationSessionEntity::class,
        ConversationTurnEntity::class,
        SavedRuleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SpeakiyDatabase : RoomDatabase() {
    abstract fun speakiyDao(): SpeakiyDao

    companion object {
        @Volatile
        private var INSTANCE: SpeakiyDatabase? = null

        fun getDatabase(context: Context): SpeakiyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpeakiyDatabase::class.java,
                    "speakiy_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
