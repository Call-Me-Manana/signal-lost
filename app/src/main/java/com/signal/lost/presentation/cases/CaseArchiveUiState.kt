package com.signal.lost.presentation.cases

import com.signal.lost.domain.model.InvestigationCase

data class CaseArchiveUiState(
    val isLoading: Boolean = true,
    val cases: List<InvestigationCase> = emptyList(),
    val errorMessage: String? = null
)
