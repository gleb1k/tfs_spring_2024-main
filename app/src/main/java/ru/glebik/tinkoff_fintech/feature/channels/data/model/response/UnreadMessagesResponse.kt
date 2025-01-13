package ru.glebik.tinkoff_fintech.feature.channels.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadMessagesResponse(
    @SerialName("result")
    val result: String?,
    @SerialName("msg")
    val msg: String?,
    @SerialName("queue_id")
    val queueId: String?,
    @SerialName("max_message_id")
    val maxMessageId: Int?,
    @SerialName("unread_msgs")
    val unreadMsgs: UnreadMsgs?,
    @SerialName("last_event_id")
    val lastEventId: Int?,
) {
    fun getUnreadCount(streamId: Int, topic: String): Int {
        val currTopic =
            unreadMsgs?.streamUnreadMsgs?.find { it.topic == topic && it.streamId == streamId }
        return currTopic?.unreadMessageIds?.size ?: 0
    }
}

@Serializable
data class UnreadMsgs(
    @SerialName("streams")
    val streamUnreadMsgs: List<StreamUnreadMsg>,
)

@Serializable
data class StreamUnreadMsg(
    @SerialName("stream_id")
    val streamId: Int?,
    @SerialName("topic")
    val topic: String?,
    @SerialName("unread_message_ids")
    val unreadMessageIds: List<Int?>?,
)