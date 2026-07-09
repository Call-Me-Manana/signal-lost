package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String,
    val name: String,
    val description: String,
    val connectedRoomIds: List<String>
)
