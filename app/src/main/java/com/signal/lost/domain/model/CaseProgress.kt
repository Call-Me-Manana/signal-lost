package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CaseProgress(
    val caseId: String,
    val status: CaseStatus,
    val viewedEvidenceIds: List<String>,
    val playerNotes: String,
    val currentHypothesis: PlayerHypothesis?
)
