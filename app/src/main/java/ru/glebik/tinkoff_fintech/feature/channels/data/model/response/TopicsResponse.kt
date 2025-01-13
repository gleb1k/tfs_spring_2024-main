package ru.glebik.tinkoff_fintech.feature.channels.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponse(
    @SerialName("msg")
    val msg: String?,
    @SerialName("result")
    val result: String?,
    @SerialName("topics")
    val topicResponses: List<TopicResponse>?,
)

@Serializable
data class TopicResponse(
    @SerialName("max_id")
    val maxId: Int?,
    @SerialName("name")
    val name: String?,
)