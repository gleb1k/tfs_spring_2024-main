package ru.glebik.tinkoff_fintech.feature.chat.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("msg")
    val msg: String?,
    @SerialName("result")
    val result: String?,
)