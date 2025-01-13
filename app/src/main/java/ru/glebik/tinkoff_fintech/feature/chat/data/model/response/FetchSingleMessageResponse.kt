package ru.glebik.tinkoff_fintech.feature.chat.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchSingleMessageResponse(
    @SerialName("message")
    val message: MessageResponse,
)