package ru.glebik.tinkoff_fintech.feature.channels.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStreamResponse(
    @SerialName("already_subscribed")
    val alreadySubscribed: Map<String, List<String>>?,
    @SerialName("msg")
    val msg: String,
    @SerialName("result")
    val result: String,
    @SerialName("code")
    val code: String? = null,
    @SerialName("subscribed")
    val subscribed: Map<String, List<String>>?,
)
