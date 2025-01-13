package ru.glebik.tinkoff_fintech.feature.chat.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
//    @SerialName("anchor")
//    val anchor: String?,
    @SerialName("found_anchor")
    val foundAnchor: Boolean?,
    @SerialName("found_newest")
    val foundNewest: Boolean?,
    @SerialName("messages")
    val messageResponses: List<MessageResponse>?,
    @SerialName("result")
    val result: String?,
)

@Serializable
data class MessageResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    @SerialName("content")
    val content: String?,

    @SerialName("sender_email")
    val senderEmail: String?,
    @SerialName("sender_full_name")
    val senderFullName: String?,
    @SerialName("sender_id")
    val senderId: Int?,

    @SerialName("display_recipient")
    val streamName: String,
    @SerialName("stream_id")
    val streamId: Int,
    @SerialName("subject")
    val topic: String?,
    @SerialName("timestamp")
    val timestamp: Int?,

    @SerialName("reactions")
    val reactionResponses: List<ReactionResponse>?,
//    //прочитано не прочитано
//    @SerialName("flags")
//    val flags: List<String?>?,
)


