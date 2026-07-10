package com.signal.lost.data.progress

import com.signal.lost.data.local.CaseProgressDao
import com.signal.lost.data.local.CaseProgressEntity

class CaseProgressRepository(
    private val dao: CaseProgressDao
) {
    suspend fun getProgress(caseId: String): CaseProgressEntity? {
        return dao.getProgress(caseId)
    }

    suspend fun saveProgress(progress: CaseProgressEntity) {
        dao.saveProgress(progress)
    }
}
