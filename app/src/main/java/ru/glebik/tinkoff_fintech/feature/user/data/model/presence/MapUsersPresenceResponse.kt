package ru.glebik.tinkoff_fintech.feature.user.data.model.presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MapUsersPresenceResponse(
    @SerialName("server_timestamp")
    val serverTimestamp: Double,
    @SerialName("presences")
    val presences: Map<String, Presence>,
)
