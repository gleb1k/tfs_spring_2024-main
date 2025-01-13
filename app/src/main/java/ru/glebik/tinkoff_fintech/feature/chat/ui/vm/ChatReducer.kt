package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import com.arkivanov.mvikotlin.core.store.Reducer

class ChatReducer : Reducer<ChatStore.State, ChatStore.Message> {

    override fun ChatStore.State.reduce(
        msg: ChatStore.Message,
    ) = when (msg) {
        is ChatStore.Message.SetError -> copy(
            errorMessage = msg.error.errorMessage,
            isLoading = false,
        )

        ChatStore.Message.SetLoading -> copy(
            isLoading = true,
            errorMessage = null,
        )

        is ChatStore.Message.SetChatFieldQuery -> copy(
            chatQuery = msg.query
        )

        is ChatStore.Message.SetEmojiSheetState -> copy(
            bottomSheetState = ChatStore.ChatBottomSheetState(
                isShow = msg.isShow, messageId = msg.messageId, type = msg.type
            )
        )

        is ChatStore.Message.SetUiList -> copy(
            isLoading = false,
            errorMessage = null,
            chatUiList = msg.uiList
        )

        is ChatStore.Message.SetTopicFieldQuery -> copy(
            topicQuery = msg.query
        )

        is ChatStore.Message.SetChatDialogQuery -> copy(
            dialogState = dialogState.copy(
                query = msg.query
            )
        )

        is ChatStore.Message.SetChatDialogVisibility -> copy(
            dialogState = dialogState.copy(
                isShow = msg.isShow,
                type = msg.type,
                messageId = msg.messageId
            )
        )
    }
}