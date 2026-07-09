package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimelineEvent(
    val id: String,
    val time: String,
    val title: String,
    val description: String,
    val locationId: String?,
    val involvedCharacterIds: List<String>,
    val sourceEvidenceId: String?,
    val reliability: EvidenceReliability
)
