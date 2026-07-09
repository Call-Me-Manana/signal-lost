package com.signal.lost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val name: String,
    val role: String,
    val description: String,
    val knownFacts: List<String>
)
