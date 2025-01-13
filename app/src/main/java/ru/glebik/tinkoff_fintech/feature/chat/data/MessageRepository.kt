package ru.glebik.tinkoff_fintech.feature.chat.data

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.network.response.BaseInfoResponse
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message


interface MessageRepository {

    suspend fun newestMessagesFlow(
        streamName: String,
        topicName: String?,
    ): Flow<ResultWrapper<List<Message>>>

    suspend fun loadMoreMessages(
        streamName: String,
        topicName: String?,
        lastMessageId: Int,
    ): ResultWrapper<List<Message>>

    suspend fun sendMessage(
        streamName: String,
        topicName: String?,
        content: String,
    ): ResultWrapper<Message>

    suspend fun addReaction(
        messageId: Int,
        emojiName: String,
    ): ResultWrapper<Message>

    suspend fun removeReaction(
        messageId: Int,
        emojiName: String,
    ): ResultWrapper<Message>

    suspend fun deleteMessage(
        messageId: Int,
    ): ResultWrapper<BaseInfoResponse>

    suspend fun editMessageContent(
        messageId: Int,
        content: String,
    ): ResultWrapper<Message>

    suspend fun editMessageTopic(
        messageId: Int,
        topicName: String,
    ): ResultWrapper<Message>

}