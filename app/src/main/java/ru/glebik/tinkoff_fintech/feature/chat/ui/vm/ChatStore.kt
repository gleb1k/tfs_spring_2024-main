package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import ru.glebik.core.presentation.mvi.ScreenState
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsStore.Intent
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.ChatItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore.ChatDialogState.Type

interface ChatStore : Store<ChatStore.Intent, ChatStore.State, ChatStore.Label> {

    data class State internal constructor(
        val topicName: String?,
        val streamName: String,

        val chatUiList: List<ChatItem> = listOf(),
        val chatQuery: String = "",

        val topicQuery: String = "",

        val bottomSheetState: ChatBottomSheetState = ChatBottomSheetState(),
        val dialogState: ChatDialogState = ChatDialogState(),

        override val isLoading: Boolean = false,
        override val errorMessage: String? = null,
    ) : ScreenState

    data class ChatBottomSheetState(
        val isShow: Boolean = false,
        val messageId: Int? = null,
        val type: Type = Type.LIST,
    ) {
        enum class Type {
            LIST, REACTIONS
        }
    }

    data class ChatDialogState(
        val query: String = "",
        val isShow: Boolean = false,
        val messageId: Int? = null,
        val type: Type = Type.EDIT_CONTENT,
    ) {
        enum class Type {
            EDIT_CONTENT, EDIT_TOPIC
        }
    }

    sealed interface Intent {
        data class OnChatQueryChange(val query: String) : Intent
        data class OnTopicQueryChange(val query: String) : Intent

        data object LoadNewestMessages : Intent
        data object LoadMoreMessages : Intent
        data class OnEmojiSheetChange(
            val isShow: Boolean,
            val messageId: Int?,
            val type: ChatBottomSheetState.Type,
        ) : Intent

        data class OnChatDialogQueryChange(val query: String) : Intent
        data class OnChatDialogVisibilityChange(
            val isShow: Boolean,
            val messageId: Int?,
            val type: Type,
        ) : Intent

        data object SendMessage : Intent
        data class DeleteMessage(val messageId: Int) : Intent
        data class EditMessageContent(val messageId: Int, val newContent: String) : Intent
        data class EditMessageTopic(val messageId: Int, val newTopicName: String) : Intent

        data class AddReaction(val messageId: Int, val emojiName: String) : Intent
        data class RemoveReaction(val messageId: Int, val emojiName: String) : Intent
    }

    sealed interface Message {
        data class SetChatFieldQuery(val query: String) : Message
        data class SetTopicFieldQuery(val query: String) : Message
        data class SetUiList(val uiList: List<ChatItem>) : Message
        data class SetEmojiSheetState(
            val isShow: Boolean,
            val messageId: Int?,
            val type: ChatBottomSheetState.Type,
        ) : Message

        data class SetChatDialogQuery(val query: String) : Message
        data class SetChatDialogVisibility(
            val isShow: Boolean,
            val messageId: Int?,
            val type: Type,
        ) : Message


        data object SetLoading : Message
        data class SetError(val error: ResultWrapper.Failed) : Message
    }

    sealed interface Label {
        data class Toast(val message: String) : Label
    }

}