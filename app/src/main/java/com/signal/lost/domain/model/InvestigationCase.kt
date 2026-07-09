package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InvestigationCase(
    val id: String,
    val title: String,
    val description: String,
    val status: CaseStatus,
    val characters: List<Character>,
    val rooms: List<Room>,
    val events: List<TimelineEvent>,
    val evidence: List<Evidence>,
    val solution: CaseSolution
)

@Serializable
enum class CaseStatus {
    AVAILABLE,
    IN_PROGRESS,
    SOLVED
}
