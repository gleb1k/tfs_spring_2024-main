package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.glebik.core.presentation.mvi.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.core.utils.parseErrorMessage
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.AddReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.DeleteMessageUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageContentUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageTopicUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.GetNewestMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.LoadMoreMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.RemoveReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.SendMessageUseCase
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.MessageItem

class ChatExecutor(
    private val messageResponseUiMapper: UiMapper<Message, MessageUiModel>,

    private val getNewestMessagesUseCase: GetNewestMessagesUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,

    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageContentUseCase: EditMessageContentUseCase,
    private val editMessageTopicUseCase: EditMessageTopicUseCase,
) : BaseExecutor<ChatStore.Intent, Unit, ChatStore.State, ChatStore.Message, ChatStore.Label>() {

    private var loadMessagesJob: Job? = null

    override fun executeIntent(intent: ChatStore.Intent, getState: () -> ChatStore.State) {
        when (intent) {
            is ChatStore.Intent.AddReaction -> addReaction(
                getState(),
                intent.emojiName,
                intent.messageId
            )

            ChatStore.Intent.LoadNewestMessages -> {
                if (loadMessagesJob?.isActive == true) return

                loadMessagesJob = scope.launchSafe {
                    loadNewestMessages(getState)
                }
            }

            is ChatStore.Intent.OnChatQueryChange -> dispatch(
                ChatStore.Message.SetChatFieldQuery(intent.query)
            )

            is ChatStore.Intent.OnEmojiSheetChange -> changeEmojiSheetState(
                intent.isShow,
                intent.messageId,
                intent.type
            )

            is ChatStore.Intent.RemoveReaction ->
                removeReaction(getState(), intent.emojiName, intent.messageId)

            ChatStore.Intent.SendMessage -> sendMessage(getState())

            is ChatStore.Intent.LoadMoreMessages -> scope.launchSafe {
                loadMoreMessages(getState())
            }

            is ChatStore.Intent.OnTopicQueryChange -> dispatch(
                ChatStore.Message.SetTopicFieldQuery(intent.query)
            )

            is ChatStore.Intent.DeleteMessage -> deleteMessage(getState(), intent.messageId)
            is ChatStore.Intent.EditMessageContent -> editMessageContent(
                getState(),
                intent.messageId,
                intent.newContent
            )

            is ChatStore.Intent.EditMessageTopic -> editMessageTopic(
                getState(),
                intent.messageId,
                intent.newTopicName
            )

            is ChatStore.Intent.OnChatDialogQueryChange -> dispatch(
                ChatStore.Message.SetChatDialogQuery(intent.query)
            )

            is ChatStore.Intent.OnChatDialogVisibilityChange -> changeChatDialogVisibility(
                isShow = intent.isShow, messageId = intent.messageId, type = intent.type
            )
        }
    }

    private fun deleteMessage(state: ChatStore.State, messageId: Int) {
        scope.launchSafe {
            when (val result = deleteMessageUseCase(messageId)) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = ChatUiListGenerator.deleteMessage(
                        stateList = state.chatUiList,
                        messageId = messageId,
                        state.topicName == null
                    )
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
    }

    private fun editMessageContent(state: ChatStore.State, messageId: Int, newContent: String) {
        scope.launchSafe {
            when (val result = editMessageContentUseCase(messageId, newContent)) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = ChatUiListGenerator.updateSingleMessage(
                        stateList = state.chatUiList,
                        newMessage = MessageItem(messageResponseUiMapper.toUi(result.data))
                    )
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
    }

    private fun editMessageTopic(state: ChatStore.State, messageId: Int, newTopicName: String) {
        scope.launchSafe {
            when (val result = editMessageTopicUseCase(messageId, newTopicName)) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = if (state.topicName == result.data.topic) {
                        ChatUiListGenerator.updateSingleMessage(
                            stateList = state.chatUiList,
                            newMessage = MessageItem(messageResponseUiMapper.toUi(result.data))
                        )
                    } else {
                        ChatUiListGenerator.deleteMessage(
                            stateList = state.chatUiList,
                            messageId = result.data.id,
                            state.topicName == null
                        )
                    }
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
    }

    private suspend fun loadMoreMessages(state: ChatStore.State) {
        val lastMessageId =
            state.chatUiList.filterIsInstance<MessageItem>().lastOrNull()?.message?.id ?: return

        when (val response = loadMoreMessagesUseCase(
            streamName = state.streamName,
            topicName = state.topicName,
            lastMessageId = lastMessageId
        )) {
            is ResultWrapper.NetworkError -> {
                publish(ChatStore.Label.Toast(parseErrorMessage(response)))
            }

            is ResultWrapper.Failed -> dispatch(ChatStore.Message.SetError(response))
            is ResultWrapper.Success -> {

                val newestMessages = response.data
                    .map { message -> MessageItem(messageResponseUiMapper.toUi(message)) }

                val resultList = ChatUiListGenerator.updateWhenCollisionPossible(
                    stateList = state.chatUiList,
                    newestList = newestMessages,
                    includeTopics = state.topicName == null
                )

                dispatch(ChatStore.Message.SetUiList(resultList))
            }
        }
    }

    private fun removeReaction(state: ChatStore.State, emojiName: String, messageId: Int) {
        scope.launchSafe {
            when (val result = removeReactionUseCase(messageId, emojiName)) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = ChatUiListGenerator.updateSingleMessage(
                        stateList = state.chatUiList,
                        newMessage = MessageItem(messageResponseUiMapper.toUi(result.data))
                    )
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
        dispatch(
            ChatStore.Message.SetEmojiSheetState(
                false,
                null,
                ChatStore.ChatBottomSheetState.Type.LIST
            )
        )
    }

    private fun addReaction(state: ChatStore.State, emojiName: String, messageId: Int) {
        scope.launchSafe {
            when (val result = addReactionUseCase(messageId, emojiName)) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = ChatUiListGenerator.updateSingleMessage(
                        stateList = state.chatUiList,
                        newMessage = MessageItem(messageResponseUiMapper.toUi(result.data))
                    )
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }

        dispatch(
            ChatStore.Message.SetEmojiSheetState(
                false,
                null,
                ChatStore.ChatBottomSheetState.Type.LIST
            )
        )
    }

    private fun changeChatDialogVisibility(
        isShow: Boolean,
        messageId: Int?,
        type: ChatStore.ChatDialogState.Type,
    ) {
        dispatch(ChatStore.Message.SetChatDialogVisibility(isShow, messageId, type))
    }

    private fun changeEmojiSheetState(
        isShow: Boolean,
        messageId: Int?,
        type: ChatStore.ChatBottomSheetState.Type,
    ) {
        dispatch(ChatStore.Message.SetEmojiSheetState(isShow, messageId, type))
    }

    private fun sendMessage(state: ChatStore.State) {
        scope.launchSafe {

            if (state.topicName == null && state.topicQuery.isBlank()) {
                publish(ChatStore.Label.Toast("Topic is required"))
                return@launchSafe
            }

            val topicToSend = state.topicName ?: state.topicQuery

            when (val result = sendMessageUseCase(
                streamName = state.streamName,
                topicName = topicToSend,
                content = state.chatQuery
            )) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {
                    val resultList = ChatUiListGenerator.updateWhenCollisionPossible(
                        stateList = state.chatUiList,
                        newestList = listOf(MessageItem(messageResponseUiMapper.toUi(result.data))),
                        includeTopics = state.topicName == null
                    )
                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
        dispatch(ChatStore.Message.SetChatFieldQuery(EMPTY_QUERY))
    }

    private suspend fun loadNewestMessages(getState: () -> ChatStore.State) {
        dispatch(ChatStore.Message.SetLoading)
        getNewestMessagesUseCase(getState().streamName, getState().topicName).collect { result ->
            when (result) {
                is ResultWrapper.NetworkError -> {
                    publish(ChatStore.Label.Toast(parseErrorMessage(result)))
                }

                is ResultWrapper.Failed -> {
                    dispatch(ChatStore.Message.SetError(result))
                }

                is ResultWrapper.Success -> {

                    val newestMessages = result.data
                        .map { message -> MessageItem(messageResponseUiMapper.toUi(message)) }

                    val resultList = ChatUiListGenerator.updateWhenCollisionPossible(
                        stateList = getState().chatUiList,
                        newestList = newestMessages,
                        includeTopics = getState().topicName == null
                    )

                    dispatch(ChatStore.Message.SetUiList(resultList))
                }
            }
        }
    }

    override fun onException(exception: Throwable) {
        super.onException(exception)

        scope.launchSafe(Dispatchers.Main) {
            dispatch(
                ChatStore.Message.SetError(
                    ResultWrapper.Failed(
                        exception = exception,
                        errorMessage = exception.message
                    )
                )
            )
        }
    }

    override fun dispose() {
        super.dispose()
        loadMessagesJob?.cancel()
    }

    companion object {
        const val EMPTY_QUERY = ""
    }
}