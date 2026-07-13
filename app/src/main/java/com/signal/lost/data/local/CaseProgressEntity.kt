package com.signal.lost.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "case_progress")
data class CaseProgressEntity(
    @PrimaryKey val caseId: String,
    val victimId: String?,
    val suspectId: String?,
    val locationId: String?,
    val timeRange: String?,
    val cause: String?,
    val method: String?,
    val selectedEvidenceIds: List<String>,
    val viewedEvidenceIds: List<String>,
    val isSolved: Boolean
)
