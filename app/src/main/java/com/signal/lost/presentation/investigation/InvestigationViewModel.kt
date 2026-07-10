package com.signal.lost.presentation.investigation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.signal.lost.data.local.CaseProgressEntity
import com.signal.lost.data.local.SignalLostDatabase
import com.signal.lost.data.progress.CaseProgressRepository
import com.signal.lost.domain.model.InvestigationCase
import com.signal.lost.domain.model.PlayerHypothesis
import com.signal.lost.domain.usecase.CheckHypothesisUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvestigationViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val checkHypothesisUseCase = CheckHypothesisUseCase()
    private val progressRepository = CaseProgressRepository(
        SignalLostDatabase.getInstance(application).caseProgressDao()
    )
    private var activeCaseId: String? = null

    var uiState by mutableStateOf(InvestigationUiState())
        private set

    fun loadProgress(caseId: String) {
        if (activeCaseId == caseId) return

        activeCaseId = caseId
        uiState = InvestigationUiState()

        viewModelScope.launch {
            val savedProgress = withContext(Dispatchers.IO) {
                progressRepository.getProgress(caseId)
            }

            if (savedProgress != null && activeCaseId == caseId) {
                uiState = savedProgress.toUiState()
            }
        }
    }

    fun selectVictim(characterId: String) {
        updateProgress {
            it.copy(victimId = characterId, checkResult = null)
        }
    }

    fun selectSuspect(characterId: String) {
        updateProgress {
            it.copy(suspectId = characterId, checkResult = null)
        }
    }

    fun selectLocation(roomId: String) {
        updateProgress {
            it.copy(locationId = roomId, checkResult = null)
        }
    }

    fun selectTimeRange(timeRange: String) {
        updateProgress {
            it.copy(timeRange = timeRange, checkResult = null)
        }
    }

    fun selectCause(cause: String) {
        updateProgress {
            it.copy(cause = cause, checkResult = null)
        }
    }

    fun selectMethod(method: String) {
        updateProgress {
            it.copy(method = method, checkResult = null)
        }
    }

    fun toggleEvidence(evidenceId: String) {
        updateProgress { currentState ->
            val selectedEvidenceIds = currentState.selectedEvidenceIds

            currentState.copy(
                selectedEvidenceIds = if (evidenceId in selectedEvidenceIds) {
                    selectedEvidenceIds - evidenceId
                } else {
                    selectedEvidenceIds + evidenceId
                },
                checkResult = null
            )
        }
    }

    fun checkHypothesis(investigationCase: InvestigationCase) {
        val hypothesis = PlayerHypothesis(
            caseId = investigationCase.id,
            victimId = uiState.victimId,
            suspectId = uiState.suspectId,
            locationId = uiState.locationId,
            timeRange = uiState.timeRange,
            cause = uiState.cause,
            method = uiState.method,
            selectedEvidenceIds = uiState.selectedEvidenceIds.toList()
        )

        val checkResult = checkHypothesisUseCase.check(
            hypothesis = hypothesis,
            solution = investigationCase.solution
        )

        updateProgress {
            it.copy(
                checkResult = checkResult,
                isSolved = checkResult.isSolved
            )
        }
    }

    private fun updateProgress(transform: (InvestigationUiState) -> InvestigationUiState) {
        uiState = transform(uiState)
        saveProgress()
    }

    private fun saveProgress() {
        val caseId = activeCaseId ?: return
        val progress = uiState.toEntity(caseId = caseId)

        viewModelScope.launch(Dispatchers.IO) {
            progressRepository.saveProgress(progress)
        }
    }

    private fun CaseProgressEntity.toUiState(): InvestigationUiState {
        return InvestigationUiState(
            victimId = victimId,
            suspectId = suspectId,
            locationId = locationId,
            timeRange = timeRange,
            cause = cause,
            method = method,
            selectedEvidenceIds = selectedEvidenceIds.toSet(),
            isSolved = isSolved
        )
    }

    private fun InvestigationUiState.toEntity(caseId: String): CaseProgressEntity {
        return CaseProgressEntity(
            caseId = caseId,
            victimId = victimId,
            suspectId = suspectId,
            locationId = locationId,
            timeRange = timeRange,
            cause = cause,
            method = method,
            selectedEvidenceIds = selectedEvidenceIds.toList(),
            isSolved = isSolved
        )
    }
}
