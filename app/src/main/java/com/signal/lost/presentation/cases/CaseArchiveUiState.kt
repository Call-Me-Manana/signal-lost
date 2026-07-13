package com.signal.lost.presentation.cases

import com.signal.lost.domain.model.InvestigationCase

data class CaseArchiveUiState(
    val isLoading: Boolean = true,
    val cases: List<InvestigationCase> = emptyList(),
    val progressByCaseId: Map<String, CaseProgressSummary> = emptyMap(),
    val errorMessage: String? = null
)

data class CaseProgressSummary(
    val viewedEvidenceCount: Int,
    val selectedEvidenceCount: Int,
    val hasHypothesis: Boolean,
    val isSolved: Boolean
)
