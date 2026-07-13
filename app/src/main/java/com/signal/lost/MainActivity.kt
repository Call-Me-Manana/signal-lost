package com.signal.lost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.signal.lost.domain.model.InvestigationCase
import com.signal.lost.presentation.cases.CaseArchiveScreen
import com.signal.lost.presentation.cases.CaseArchiveViewModel
import com.signal.lost.presentation.investigation.InvestigationScreen
import com.signal.lost.presentation.main.MainScreen
import com.signal.lost.presentation.theme.SignalLostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignalLostTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SignalLostApp()
                }
            }
        }
    }
}

@Composable
private fun SignalLostApp() {
    var currentScreen by remember { mutableStateOf(SignalLostScreen.Main) }
    var selectedCase by remember { mutableStateOf<InvestigationCase?>(null) }

    when (currentScreen) {
        SignalLostScreen.Main -> {
            MainScreen(
                onOpenCaseArchive = {
                    currentScreen = SignalLostScreen.CaseArchive
                }
            )
        }

        SignalLostScreen.CaseArchive -> {
            val viewModel: CaseArchiveViewModel = viewModel()

            CaseArchiveScreen(
                uiState = viewModel.uiState,
                onBack = {
                    currentScreen = SignalLostScreen.Main
                },
                onOpenCase = { investigationCase ->
                    selectedCase = investigationCase
                    currentScreen = SignalLostScreen.Investigation
                },
                onRefreshProgress = viewModel::refreshProgress
            )
        }

        SignalLostScreen.Investigation -> {
            val investigationCase = selectedCase

            if (investigationCase == null) {
                MainScreen(
                    onOpenCaseArchive = {
                        currentScreen = SignalLostScreen.CaseArchive
                    }
                )
            } else {
                InvestigationScreen(
                    investigationCase = investigationCase,
                    onBack = {
                        currentScreen = SignalLostScreen.CaseArchive
                    }
                )
            }
        }
    }
}

private enum class SignalLostScreen {
    Main,
    CaseArchive,
    Investigation
}
