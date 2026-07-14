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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.signal.lost.domain.model.Character
import com.signal.lost.domain.model.Evidence
import com.signal.lost.domain.model.EvidenceReliability
import com.signal.lost.domain.model.EvidenceSourceType
import com.signal.lost.domain.model.InvestigationCase
import com.signal.lost.domain.model.Room
import com.signal.lost.domain.model.TimelineEvent
import com.signal.lost.domain.usecase.HypothesisCheckResult

@Composable
fun InvestigationScreen(
    investigationCase: InvestigationCase,
    onBack: () -> Unit
) {
    val viewModel: InvestigationViewModel = viewModel(key = investigationCase.id)
    val uiState = viewModel.uiState
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedEvidenceId by remember { mutableStateOf<String?>(null) }
    val tabs = listOf("Улики", "Таймлайн", "Экипаж", "Гипотеза")

    LaunchedEffect(investigationCase.id) {
        viewModel.loadProgress(investigationCase.id)
    }

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
                evidence = investigationCase.evidence,
                selectedEvidenceId = selectedEvidenceId,
                viewedEvidenceIds = uiState.viewedEvidenceIds,
                onEvidenceSelected = { evidenceId ->
                    selectedEvidenceId = evidenceId
                    viewModel.markEvidenceViewed(evidenceId)
                },
                onCloseEvidence = {
                    selectedEvidenceId = null
                }
            )

            1 -> TimelineList(
                modifier = Modifier.weight(1f),
                events = investigationCase.events
            )

            2 -> CharacterList(
                modifier = Modifier.weight(1f),
                characters = investigationCase.characters
            )

            3 -> HypothesisPanel(
                modifier = Modifier.weight(1f),
                investigationCase = investigationCase,
                uiState = uiState,
                onVictimSelected = viewModel::selectVictim,
                onSuspectSelected = viewModel::selectSuspect,
                onLocationSelected = viewModel::selectLocation,
                onTimeRangeSelected = viewModel::selectTimeRange,
                onCauseSelected = viewModel::selectCause,
                onMethodSelected = viewModel::selectMethod,
                onEvidenceToggled = viewModel::toggleEvidence,
                onCheckHypothesis = {
                    viewModel.checkHypothesis(investigationCase)
                }
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
            Text(text = "Назад")
        }
    }
}

@Composable
private fun EvidenceList(
    modifier: Modifier = Modifier,
    evidence: List<Evidence>,
    selectedEvidenceId: String?,
    viewedEvidenceIds: Set<String>,
    onEvidenceSelected: (String) -> Unit,
    onCloseEvidence: () -> Unit
) {
    val selectedEvidence = evidence.firstOrNull { item -> item.id == selectedEvidenceId }

    if (selectedEvidence != null) {
        EvidenceDetail(
            modifier = modifier,
            evidence = selectedEvidence,
            isViewed = selectedEvidence.id in viewedEvidenceIds,
            onClose = onCloseEvidence
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(evidence) { item ->
                EvidenceListItem(
                    evidence = item,
                    isViewed = item.id in viewedEvidenceIds,
                    onOpen = { onEvidenceSelected(item.id) }
                )
            }
        }
    }
}

@Composable
private fun EvidenceListItem(
    evidence: Evidence,
    isViewed: Boolean,
    onOpen: () -> Unit
) {
    InvestigationCard {
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
                    text = evidence.sourceType.label,
                    color = Color(0xFF8BE9FD),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = evidence.title,
                    color = Color(0xFFE6F7FF),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Надежность: ${evidence.reliability.label}",
                    color = Color(0xFFB8D8E0),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (isViewed) "Статус: ПРОСМОТРЕНО" else "Статус: НОВОЕ",
                    color = if (isViewed) Color(0xFFB8D8E0) else Color(0xFFFFD166),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                modifier = Modifier.padding(start = 12.dp),
                onClick = onOpen
            ) {
                Text(text = "Открыть")
            }
        }
    }
}

@Composable
private fun EvidenceDetail(
    modifier: Modifier = Modifier,
    evidence: Evidence,
    isViewed: Boolean,
    onClose: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            InvestigationCard {
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
                            text = evidence.sourceType.label,
                            color = Color(0xFF8BE9FD),
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = evidence.title,
                            color = Color(0xFFE6F7FF),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Button(
                        modifier = Modifier.padding(start = 12.dp),
                        onClick = onClose
                    ) {
                        Text(text = "Закрыть")
                    }
                }
            }
        }

        item {
            InvestigationCard {
                Text(
                    text = evidence.content,
                    color = Color(0xFFA7B6BE),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Надежность: ${evidence.reliability.label}",
                    color = Color(0xFFB8D8E0),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (isViewed) "Статус: ПРОСМОТРЕНО" else "Статус: НОВОЕ",
                    color = if (isViewed) Color(0xFFB8D8E0) else Color(0xFFFFD166),
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
                    text = "Надежность: ${event.reliability.label}",
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
private fun HypothesisPanel(
    modifier: Modifier = Modifier,
    investigationCase: InvestigationCase,
    uiState: InvestigationUiState,
    onVictimSelected: (String) -> Unit,
    onSuspectSelected: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onTimeRangeSelected: (String) -> Unit,
    onCauseSelected: (String) -> Unit,
    onMethodSelected: (String) -> Unit,
    onEvidenceToggled: (String) -> Unit,
    onCheckHypothesis: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            InvestigationCard {
                Text(
                    text = "ФИНАЛЬНАЯ ГИПОТЕЗА",
                    color = Color(0xFF8BE9FD),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Выберите полную цепочку событий и приложите улики, которые ее подтверждают.",
                    color = Color(0xFFA7B6BE),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (uiState.isSolved) {
                    Text(
                        text = "Сохраненный статус: ДЕЛО ЗАКРЫТО",
                        color = Color(0xFF8BE9FD),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        item {
            CharacterChoiceSection(
                title = "Жертва",
                characters = investigationCase.characters,
                selectedCharacterId = uiState.victimId,
                onSelected = onVictimSelected
            )
        }

        item {
            CharacterChoiceSection(
                title = "Причастный",
                characters = investigationCase.characters,
                selectedCharacterId = uiState.suspectId,
                onSelected = onSuspectSelected
            )
        }

        item {
            RoomChoiceSection(
                title = "Место",
                rooms = investigationCase.rooms,
                selectedRoomId = uiState.locationId,
                onSelected = onLocationSelected
            )
        }

        item {
            TextChoiceSection(
                title = "Время",
                options = investigationCase.hypothesisOptions.timeRanges,
                selectedOption = uiState.timeRange,
                onSelected = onTimeRangeSelected
            )
        }

        item {
            TextChoiceSection(
                title = "Причина",
                options = investigationCase.hypothesisOptions.causes,
                selectedOption = uiState.cause,
                onSelected = onCauseSelected
            )
        }

        item {
            TextChoiceSection(
                title = "Способ",
                options = investigationCase.hypothesisOptions.methods,
                selectedOption = uiState.method,
                onSelected = onMethodSelected
            )
        }

        item {
            EvidenceChoiceSection(
                evidence = investigationCase.evidence,
                selectedEvidenceIds = uiState.selectedEvidenceIds,
                onEvidenceToggled = onEvidenceToggled
            )
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCheckHypothesis
            ) {
                Text(text = "Проверить гипотезу")
            }
        }

        uiState.checkResult?.let { result ->
            item {
                HypothesisResultCard(
                    result = result,
                    evidence = investigationCase.evidence
                )
            }
        }
    }
}

@Composable
private fun CharacterChoiceSection(
    title: String,
    characters: List<Character>,
    selectedCharacterId: String?,
    onSelected: (String) -> Unit
) {
    IdChoiceSection(
        title = title,
        options = characters.map { character -> character.id to character.name },
        selectedOptionId = selectedCharacterId,
        onSelected = onSelected
    )
}

@Composable
private fun RoomChoiceSection(
    title: String,
    rooms: List<Room>,
    selectedRoomId: String?,
    onSelected: (String) -> Unit
) {
    IdChoiceSection(
        title = title,
        options = rooms.map { room -> room.id to room.name },
        selectedOptionId = selectedRoomId,
        onSelected = onSelected
    )
}

@Composable
private fun TextChoiceSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onSelected: (String) -> Unit
) {
    IdChoiceSection(
        title = title,
        options = options.map { option -> option to option },
        selectedOptionId = selectedOption,
        onSelected = onSelected
    )
}

@Composable
private fun IdChoiceSection(
    title: String,
    options: List<Pair<String, String>>,
    selectedOptionId: String?,
    onSelected: (String) -> Unit
) {
    InvestigationCard {
        Text(
            text = title,
            color = Color(0xFFE6F7FF),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        options.forEach { (id, label) ->
            FilterChip(
                selected = selectedOptionId == id,
                onClick = { onSelected(id) },
                label = { Text(text = label) }
            )
        }
    }
}

@Composable
private fun EvidenceChoiceSection(
    evidence: List<Evidence>,
    selectedEvidenceIds: Set<String>,
    onEvidenceToggled: (String) -> Unit
) {
    InvestigationCard {
        Text(
            text = "Улики",
            color = Color(0xFFE6F7FF),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        evidence.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.id in selectedEvidenceIds,
                    onCheckedChange = { onEvidenceToggled(item.id) }
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        color = Color(0xFFE6F7FF),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.sourceType.label,
                        color = Color(0xFF8BE9FD),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private val EvidenceSourceType.label: String
    get() = when (this) {
        EvidenceSourceType.DOOR_LOG -> "Журнал дверей"
        EvidenceSourceType.SYSTEM_LOG -> "Системный журнал"
        EvidenceSourceType.AUDIO_TRANSCRIPT -> "Расшифровка аудио"
        EvidenceSourceType.CAMERA_RECORDING -> "Запись камеры"
        EvidenceSourceType.SENSOR_DATA -> "Данные датчиков"
        EvidenceSourceType.MEDICAL_REPORT -> "Медицинский отчет"
        EvidenceSourceType.ACCESS_LOG -> "Журнал доступа"
        EvidenceSourceType.AI_MEMORY_FRAGMENT -> "Фрагмент памяти ИИ"
    }

private val EvidenceReliability.label: String
    get() = when (this) {
        EvidenceReliability.CONFIRMED -> "подтверждено"
        EvidenceReliability.PARTIAL -> "частично"
        EvidenceReliability.CONFLICTING -> "противоречиво"
        EvidenceReliability.CORRUPTED -> "повреждено"
        EvidenceReliability.UNKNOWN -> "неизвестно"
    }

@Composable
private fun HypothesisResultCard(
    result: HypothesisCheckResult,
    evidence: List<Evidence>
) {
    val evidenceTitleById = evidence.associate { item -> item.id to item.title }

    InvestigationCard {
        Text(
            text = if (result.isSolved) {
                "ДЕЛО ЗАКРЫТО"
            } else {
                "ГИПОТЕЗА НЕПОЛНАЯ"
            },
            color = if (result.isSolved) Color(0xFF8BE9FD) else Color(0xFFFFD166),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        result.checks.forEach { item ->
            Text(
                text = "${if (item.isCorrect) "[ВЕРНО]" else "[--]"} ${item.title}",
                color = if (item.isCorrect) Color(0xFFB8D8E0) else Color(0xFFFFD166),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (result.missingEvidenceIds.isNotEmpty()) {
            val missingEvidenceTitles = result.missingEvidenceIds
                .map { evidenceId -> evidenceTitleById[evidenceId] ?: evidenceId }
                .joinToString()

            Text(
                text = "Не хватает обязательных улик: $missingEvidenceTitles",
                color = Color(0xFFFFB4AB),
                style = MaterialTheme.typography.bodySmall
            )
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
