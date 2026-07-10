package com.signal.lost.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CaseProgressEntity::class],
    version = 1,
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
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
