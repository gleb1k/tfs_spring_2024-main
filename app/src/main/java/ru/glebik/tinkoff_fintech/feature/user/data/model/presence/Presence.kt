package ru.glebik.tinkoff_fintech.feature.user.data.model.presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Presence(
    @SerialName("aggregated")
    val aggregated: Aggregated?,
)

@Serializable
data class Aggregated(
    @SerialName("status")
    val status: String?,
    @SerialName("timestamp")
    val timestamp: Int?,
)
