package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Evidence(
    val id: String,
    val title: String,
    val content: String,
    val sourceType: EvidenceSourceType,
    val relatedCharacterIds: List<String>,
    val relatedRoomIds: List<String>,
    val relatedEventIds: List<String>,
    val reliability: EvidenceReliability
)

@Serializable
enum class EvidenceSourceType {
    DOOR_LOG,
    SYSTEM_LOG,
    AUDIO_TRANSCRIPT,
    CAMERA_RECORDING,
    SENSOR_DATA,
    MEDICAL_REPORT,
    ACCESS_LOG,
    AI_MEMORY_FRAGMENT
}

@Serializable
enum class EvidenceReliability {
    CONFIRMED,
    PARTIAL,
    CONFLICTING,
    CORRUPTED,
    UNKNOWN
}
