package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.glebik.core.presentation.mvi.MviViewModel
import ru.glebik.core.utils.orEmpty
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.MessageItem

class ChatViewModel(
    private val store: ChatStore,
) : MviViewModel<ChatStore.Intent, ChatStore.State,  ChatStore.Label>(
    store
) {

    init {
        listenToNewestMessages()
    }

    fun listenToNewestMessages() = store.accept(ChatStore.Intent.LoadNewestMessages)

    fun loadMoreMessages() = store.accept(ChatStore.Intent.LoadMoreMessages)

    fun showBottomSheetList(messageId: Int?) = store.accept(
        ChatStore.Intent.OnEmojiSheetChange(
            true,
            messageId,
            ChatStore.ChatBottomSheetState.Type.LIST
        )
    )

    fun showBottomSheetReactions(messageId: Int?) = store.accept(
        ChatStore.Intent.OnEmojiSheetChange(
            true,
            messageId,
            ChatStore.ChatBottomSheetState.Type.REACTIONS
        )
    )

    fun hideBottomSheet() = store.accept(
        ChatStore.Intent.OnEmojiSheetChange(
            isShow = false,
            messageId = null,
            type = ChatStore.ChatBottomSheetState.Type.LIST
        )
    )

    fun changeEmojiSheetType(type: ChatStore.ChatBottomSheetState.Type) = store.accept(
        ChatStore.Intent.OnEmojiSheetChange(
            isShow = state.value.bottomSheetState.isShow,
            messageId = state.value.bottomSheetState.messageId,
            type = type
        )
    )

    fun removeReaction(messageId: Int, emojiName: String) =
        store.accept(ChatStore.Intent.RemoveReaction(messageId, emojiName))

    fun addReaction(emojiName: String, messageId: Int) =
        store.accept(ChatStore.Intent.AddReaction(messageId, emojiName))

    fun onReactionClick(message: MessageUiModel, reaction: ReactionUiModel) {
        if (reaction.isSelected) {
            removeReaction(message.id, reaction.emojiName)
        } else {
            addReaction(reaction.emojiName, message.id)
        }
    }

    fun sendMessage() =
        store.accept(ChatStore.Intent.SendMessage)

    fun onChatQueryChange(newValue: String) =
        store.accept(ChatStore.Intent.OnChatQueryChange(newValue))

    fun onTopicQueryChange(newValue: String) =
        store.accept(ChatStore.Intent.OnTopicQueryChange(newValue))

    fun getMessageContentById(id: Int): AnnotatedString {
        val message =
            state.value.chatUiList.filterIsInstance<MessageItem>().find { it.message.id == id }
                ?: return AnnotatedString("")

        return AnnotatedString(message.message.content)
    }

    fun deleteMessage() =
        store.accept(ChatStore.Intent.DeleteMessage(state.value.bottomSheetState.messageId.orEmpty()))

    fun editMessageContentClick() {
        store.accept(
            ChatStore.Intent.EditMessageContent(
                state.value.dialogState.messageId.orEmpty(),
                state.value.dialogState.query
            )
        )
        hideChatDialog()
    }

    fun editMessageTopicClick() {
        store.accept(
            ChatStore.Intent.EditMessageTopic(
                state.value.dialogState.messageId.orEmpty(),
                state.value.dialogState.query
            )
        )
        hideChatDialog()
    }

    fun onChatDialogQueryChange(newValue: String) =
        store.accept(ChatStore.Intent.OnChatDialogQueryChange(newValue))

    fun onShowEditContentChatDialog(messageId: Int) {
        val message = store.state.chatUiList
            .filterIsInstance<MessageItem>()
            .find { it.message.id == messageId }

        store.accept(ChatStore.Intent.OnChatDialogQueryChange(message?.message?.content.orEmpty()))
        store.accept(
            ChatStore.Intent.OnChatDialogVisibilityChange(
                true,
                messageId,
                ChatStore.ChatDialogState.Type.EDIT_CONTENT
            )
        )
    }

    fun onShowChangeTopicChatDialog(messageId: Int) {
        val message = store.state.chatUiList
            .filterIsInstance<MessageItem>()
            .find { it.message.id == messageId }

        store.accept(ChatStore.Intent.OnChatDialogQueryChange(message?.message?.topic.orEmpty()))
        store.accept(
            ChatStore.Intent.OnChatDialogVisibilityChange(
                true,
                messageId,
                ChatStore.ChatDialogState.Type.EDIT_TOPIC
            )
        )
    }

    fun hideChatDialog() =
        store.accept(
            ChatStore.Intent.OnChatDialogVisibilityChange(
                false,
                null,
                ChatStore.ChatDialogState.Type.EDIT_CONTENT
            )
        )


    companion object {

        fun provideFactory(
            store: ChatStore,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ChatViewModel(
                    store
                )
            }
        }
    }
}