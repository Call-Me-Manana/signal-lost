package com.signal.lost.presentation.investigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.signal.lost.domain.model.Character
import com.signal.lost.domain.model.Evidence
import com.signal.lost.domain.model.InvestigationCase
import com.signal.lost.domain.model.TimelineEvent

@Composable
fun InvestigationScreen(
    investigationCase: InvestigationCase,
    onBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Evidence", "Timeline", "Crew")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071013))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InvestigationHeader(
            title = investigationCase.title,
            description = investigationCase.description,
            onBack = onBack
        )

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> EvidenceList(
                modifier = Modifier.weight(1f),
                evidence = investigationCase.evidence
            )

            1 -> TimelineList(
                modifier = Modifier.weight(1f),
                events = investigationCase.events
            )

            2 -> CharacterList(
                modifier = Modifier.weight(1f),
                characters = investigationCase.characters
            )
        }
    }
}

@Composable
private fun InvestigationHeader(
    title: String,
    description: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFFE6F7FF),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = description,
                color = Color(0xFFA7B6BE),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Button(
            modifier = Modifier.padding(start = 12.dp),
            onClick = onBack
        ) {
            Text(text = "Back")
        }
    }
}

@Composable
private fun EvidenceList(
    modifier: Modifier = Modifier,
    evidence: List<Evidence>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(evidence) { item ->
            InvestigationCard {
                Text(
                    text = item.sourceType.name,
                    color = Color(0xFF8BE9FD),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = item.title,
                    color = Color(0xFFE6F7FF),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.content,
                    color = Color(0xFFA7B6BE),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Reliability: ${item.reliability.name}",
                    color = Color(0xFFB8D8E0),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TimelineList(
    modifier: Modifier = Modifier,
    events: List<TimelineEvent>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { event ->
            InvestigationCard {
                Text(
                    text = event.time,
                    color = Color(0xFF8BE9FD),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = event.title,
                    color = Color(0xFFE6F7FF),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = event.description,
                    color = Color(0xFFA7B6BE),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Reliability: ${event.reliability.name}",
                    color = Color(0xFFB8D8E0),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun CharacterList(
    modifier: Modifier = Modifier,
    characters: List<Character>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(characters) { character ->
            InvestigationCard {
                Text(
                    text = character.name,
                    color = Color(0xFFE6F7FF),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = character.role,
                    color = Color(0xFF8BE9FD),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = character.description,
                    color = Color(0xFFA7B6BE),
                    style = MaterialTheme.typography.bodyMedium
                )
                character.knownFacts.forEach { fact ->
                    Text(
                        text = "- $fact",
                        color = Color(0xFFB8D8E0),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun InvestigationCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E1A1F)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}
