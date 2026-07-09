package com.signal.lost.presentation.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.signal.lost.domain.model.CaseStatus
import com.signal.lost.domain.model.InvestigationCase
import com.signal.lost.presentation.theme.SignalLostTheme

@Composable
fun CaseArchiveScreen(
    uiState: CaseArchiveUiState,
    onBack: () -> Unit,
    onOpenCase: (InvestigationCase) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071013))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CASE ARCHIVE",
                    color = Color(0xFFE6F7FF),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Recovered investigation packets",
                    color = Color(0xFF8EA4AD),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(onClick = onBack) {
                Text(text = "Back")
            }
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(color = Color(0xFF8BE9FD))
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage,
                    color = Color(0xFFFFB4AB)
                )
            }

            else -> {
                uiState.cases.forEach { investigationCase ->
                    CaseArchiveItem(
                        investigationCase = investigationCase,
                        onOpenCase = onOpenCase
                    )
                }
            }
        }
    }
}

@Composable
private fun CaseArchiveItem(
    investigationCase: InvestigationCase,
    onOpenCase: (InvestigationCase) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E1A1F)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = investigationCase.title,
                    color = Color(0xFFE6F7FF),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = {},
                    label = { Text(text = investigationCase.status.label) }
                )
            }
            Text(
                text = investigationCase.description,
                color = Color(0xFFA7B6BE),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${investigationCase.characters.size} profiles / ${investigationCase.evidence.size} evidence files / ${investigationCase.events.size} timeline events",
                color = Color(0xFF8BE9FD),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onOpenCase(investigationCase) }
            ) {
                Text(text = "Begin recovery")
            }
        }
    }
}

private val CaseStatus.label: String
    get() = when (this) {
        CaseStatus.AVAILABLE -> "Available"
        CaseStatus.IN_PROGRESS -> "In progress"
        CaseStatus.SOLVED -> "Solved"
    }

@Preview(showBackground = true)
@Composable
private fun CaseArchiveScreenPreview() {
    SignalLostTheme {
        CaseArchiveScreen(
            uiState = CaseArchiveUiState(
                isLoading = false,
                cases = emptyList()
            ),
            onBack = {},
            onOpenCase = {}
        )
    }
}
