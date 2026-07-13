package com.signal.lost.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [CaseProgressEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class SignalLostDatabase : RoomDatabase() {
    abstract fun caseProgressDao(): CaseProgressDao

    companion object {
        @Volatile
        private var instance: SignalLostDatabase? = null

        fun getInstance(context: Context): SignalLostDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SignalLostDatabase::class.java,
                    "signal_lost.db"
                )
                    .addMigrations(Migration1To2)
                    .build()
                    .also { database ->
                        instance = database
                    }
            }
        }

        private val Migration1To2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE case_progress ADD COLUMN viewedEvidenceIds TEXT NOT NULL DEFAULT '[]'"
                )
            }
        }
    }
}
