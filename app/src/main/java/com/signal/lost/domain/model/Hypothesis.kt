package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerHypothesis(
    val caseId: String,
    val victimId: String?,
    val suspectId: String?,
    val locationId: String?,
    val timeRange: String?,
    val cause: String?,
    val method: String?,
    val selectedEvidenceIds: List<String>
)

@Serializable
data class CaseSolution(
    val victimId: String,
    val suspectId: String,
    val locationId: String,
    val timeRange: String,
    val cause: String,
    val method: String,
    val requiredEvidenceIds: List<String>
)
