package com.signal.lost.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CaseProgressDao {
    @Query("SELECT * FROM case_progress WHERE caseId = :caseId")
    suspend fun getProgress(caseId: String): CaseProgressEntity?

    @Upsert
    suspend fun saveProgress(progress: CaseProgressEntity)
}
