package ru.glebik.tinkoff_fintech.feature.chat.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.glebik.core.network.response.BaseInfoResponse
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.core.utils.wrapError
import ru.glebik.tinkoff_fintech.feature.chat.data.model.Narrow
import ru.glebik.tinkoff_fintech.feature.chat.data.model.NarrowItem
import ru.glebik.tinkoff_fintech.feature.chat.data.model.mapper.toDomain
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import javax.inject.Inject

@FeatureScope
class MessageRepositoryImpl @Inject constructor(
    @IoDispatcherQualifier
    private val ioDispatcher: CoroutineDispatcher,

    private val messagesApi: MessagesApi,
    private val chatLongPollService: ChatLongPollService,
    private val messagesCacheManager: MessagesCacheManager,
) : MessageRepository {

    override suspend fun newestMessagesFlow(
        streamName: String,
        topicName: String?,
    ): Flow<ResultWrapper<List<Message>>> = flow {
        runCatching {
            withContext(ioDispatcher) { messagesCacheManager.get(streamName, topicName) }
        }.fold(
            onSuccess = {
                if (it.isNotEmpty()) {
                    emit(ResultWrapper.Success(it))
                }
            },
            onFailure = { wrapError(it) }
        )

        withContext(ioDispatcher) {
            chatLongPollService.fetchNewestMessages(streamName, topicName)
        }.map { result ->
            when (result) {
                is ResultWrapper.Failed -> result
                is ResultWrapper.Success -> {

                    val messagesResponses = result.data.messageResponses ?: listOf()

                    messagesCacheManager.put(messagesResponses, streamName, topicName)

                    ResultWrapper.Success(
                        result.data.messageResponses
                            ?.map { it.toDomain() }
                            ?: listOf()
                    )
                }
            }
        }.collect {
            emit(it)
        }
    }

    override suspend fun loadMoreMessages(
        streamName: String,
        topicName: String?,
        lastMessageId: Int,
    ): ResultWrapper<List<Message>> {
        val narrow = Narrow()
        if (topicName != null) {
            narrow.add(NarrowItem(Narrow.TOPIC, topicName))
        }
        narrow.add(NarrowItem(Narrow.STREAM, streamName))

        return withContext(ioDispatcher) {
            runCatching {
                val messagesResponses = messagesApi.getMessages(
                    anchor = lastMessageId.toString(),
                    numBefore = MessagesApi.BASE_MESSAGE_FETCH_COUNT,
                    numAfter = 0,
                    narrow = narrow.toString(),
                ).messageResponses ?: listOf()

                messagesCacheManager.put(messagesResponses, streamName, topicName)

                messagesResponses
                    .map { it.toDomain() }
            }.fold(
                onSuccess = { ResultWrapper.Success(it) },
                onFailure = { wrapError(it) }
            )
        }
    }

    override suspend fun sendMessage(
        streamName: String,
        topicName: String?,
        content: String,
    ): ResultWrapper<Message> =
        withContext(ioDispatcher) {
            runCatching {
                val sendMessageResponse = messagesApi.sendMessage(
                    type = SEND_MESSAGE_TYPE_STREAM,
                    to = streamName,
                    topic = topicName,
                    content = content
                )

                messagesApi.getMessageById(
                    sendMessageResponse.id!!,
                ).message.toDomain()
            }.fold(
                onFailure = {
                    Log.e("sendedMessage", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    Log.d("sendedMessage", it.toString())
                    ResultWrapper.Success(it)
                }
            )
        }

    override suspend fun addReaction(messageId: Int, emojiName: String): ResultWrapper<Message> =
        withContext(ioDispatcher) {
            runCatching {
                messagesApi.addReaction(
                    messageId = messageId, emojiName = emojiName
                )

                messagesApi.getMessageById(
                    messageId,
                ).message.toDomain()
            }.fold(
                onFailure = {
                    Log.e("addReaction", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    ResultWrapper.Success(it)
                }
            )

        }

    override suspend fun removeReaction(messageId: Int, emojiName: String) =
        withContext(ioDispatcher) {
            runCatching {
                messagesApi.removeReaction(
                    messageId = messageId, emojiName = emojiName
                )

                messagesApi.getMessageById(
                    messageId,
                ).message.toDomain()
            }.fold(
                onFailure = {
                    Log.e("removeReaction", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    Log.d("removeReaction", it.toString())
                    ResultWrapper.Success(it)
                }
            )
        }

    override suspend fun deleteMessage(messageId: Int): ResultWrapper<BaseInfoResponse> =
        withContext(ioDispatcher)
        {
            runCatching {
                messagesApi.deleteMessage(
                    messageId = messageId
                )
            }.fold(
                onFailure = {
                    Log.e("deleteMessage", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    Log.d("deleteMessage", it.toString())
                    messagesCacheManager.deleteMessage(messageId)
                    ResultWrapper.Success(it)
                }
            )
        }


    override suspend fun editMessageContent(
        messageId: Int,
        content: String,
    ): ResultWrapper<Message> =
        withContext(ioDispatcher) {
            runCatching {
                messagesApi.editMessageContent(
                    messageId = messageId, content = content
                )

                messagesApi.getMessageById(
                    messageId,
                ).message.toDomain()
            }.fold(
                onFailure = {
                    Log.e("editMessageContent", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    Log.d("editMessageContent", it.toString())
                    ResultWrapper.Success(it)
                }
            )
        }

    override suspend fun editMessageTopic(
        messageId: Int,
        topicName: String,
    ): ResultWrapper<Message> =
        withContext(ioDispatcher) {
            runCatching {
                messagesApi.editMessageTopic(
                    messageId = messageId, topic = topicName
                )

                messagesApi.getMessageById(
                    messageId,
                ).message.toDomain()
            }.fold(
                onFailure = {
                    Log.e("editMessageTopic", it.toString())
                    wrapError(it)
                },
                onSuccess = {
                    Log.d("editMessageTopic", it.toString())
                    ResultWrapper.Success(it)
                }
            )
        }

    companion object {
        const val SEND_MESSAGE_TYPE_STREAM = "stream"
    }
}
