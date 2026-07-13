package com.signal.lost.presentation.investigation

import com.signal.lost.domain.usecase.HypothesisCheckResult

data class InvestigationUiState(
    val victimId: String? = null,
    val suspectId: String? = null,
    val locationId: String? = null,
    val timeRange: String? = null,
    val cause: String? = null,
    val method: String? = null,
    val selectedEvidenceIds: Set<String> = emptySet(),
    val viewedEvidenceIds: Set<String> = emptySet(),
    val isSolved: Boolean = false,
    val checkResult: HypothesisCheckResult? = null
)
