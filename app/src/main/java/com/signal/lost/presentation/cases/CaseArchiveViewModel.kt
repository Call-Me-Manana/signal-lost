package com.signal.lost.presentation.cases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.signal.lost.data.cases.AssetCaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CaseArchiveViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository = AssetCaseRepository(application)

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
                    repository.loadCases()
                }
            }.onSuccess { cases ->
                uiState = CaseArchiveUiState(
                    isLoading = false,
                    cases = cases
                )
            }.onFailure { error ->
                uiState = CaseArchiveUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Case archive is unavailable."
                )
            }
        }
    }
}
