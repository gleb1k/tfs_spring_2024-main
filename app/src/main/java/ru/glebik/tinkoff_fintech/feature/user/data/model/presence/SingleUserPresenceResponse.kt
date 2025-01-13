package ru.glebik.tinkoff_fintech.feature.user.data.model.presence


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleUserPresenceResponse(
    @SerialName("msg")
    val msg: String?,
    @SerialName("presence")
    val presence: Presence?,
    @SerialName("result")
    val result: String?,
)

