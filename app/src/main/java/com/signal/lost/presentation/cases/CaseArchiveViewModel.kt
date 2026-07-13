package com.signal.lost.presentation.cases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.signal.lost.data.cases.AssetCaseRepository
import com.signal.lost.data.local.CaseProgressEntity
import com.signal.lost.data.local.SignalLostDatabase
import com.signal.lost.data.progress.CaseProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CaseArchiveViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val caseRepository = AssetCaseRepository(application)
    private val progressRepository = CaseProgressRepository(
        SignalLostDatabase.getInstance(application).caseProgressDao()
    )

    var uiState by mutableStateOf(CaseArchiveUiState())
        private set

    init {
        loadCases()
    }

    private fun loadCases() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            runCatching {
                withContext(Dispatchers.IO) {
                    val cases = caseRepository.loadCases()
                    val progress = progressRepository.getAllProgress()

                    cases to progress
                }
            }.onSuccess { (cases, progress) ->
                uiState = CaseArchiveUiState(
                    isLoading = false,
                    cases = cases,
                    progressByCaseId = progress.toSummaryMap()
                )
            }.onFailure { error ->
                uiState = CaseArchiveUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Архив дел недоступен."
                )
            }
        }
    }

    fun refreshProgress() {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    progressRepository.getAllProgress()
                }
            }.onSuccess { progress ->
                uiState = uiState.copy(
                    progressByCaseId = progress.toSummaryMap()
                )
            }
        }
    }

    private fun List<CaseProgressEntity>.toSummaryMap(): Map<String, CaseProgressSummary> {
        return associate { progress ->
            progress.caseId to CaseProgressSummary(
                viewedEvidenceCount = progress.viewedEvidenceIds.size,
                selectedEvidenceCount = progress.selectedEvidenceIds.size,
                hasHypothesis = progress.hasHypothesis,
                isSolved = progress.isSolved
            )
        }
    }

    private val CaseProgressEntity.hasHypothesis: Boolean
        get() = victimId != null ||
            suspectId != null ||
            locationId != null ||
            timeRange != null ||
            cause != null ||
            method != null ||
            selectedEvidenceIds.isNotEmpty()
}
