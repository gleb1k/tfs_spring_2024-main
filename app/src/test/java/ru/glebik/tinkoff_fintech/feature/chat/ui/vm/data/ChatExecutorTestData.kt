package ru.glebik.tinkoff_fintech.feature.chat.ui.vm.data

import com.arkivanov.mvikotlin.core.store.Executor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.AddReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.GetNewestMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.LoadMoreMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.RemoveReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.SendMessageUseCase
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.mapper.MessageUiMapper
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.ChatItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatExecutor
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore

class ChatExecutorTestData {

    val stream = "stream"
    val topic = "topic"

    val messageId = 1
    val emojiName = "smile"
    val query = "123"
    val isShow = true
    val uiList: List<ChatItem> = mockk()
    val errorMessage = "error"
    val error = ResultWrapper.Failed(Throwable(errorMessage), errorMessage)

    private val dateUiMapper: DateUiMapper = mockk()
    private val messageResponseUiMapper: MessageUiMapper = mockk()

    //    private val getNewestMessagesUseCase: GetNewestMessagesUseCase = mockk()
    private val addReactionUseCase: AddReactionUseCase = mockk()
    private val removeReactionUseCase: RemoveReactionUseCase = mockk()
    private val sendMessageUseCase: SendMessageUseCase = mockk()
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase = mockk()

    fun executor(): ChatExecutor {

        val getNewestMessagesUseCase: GetNewestMessagesUseCase = mockk()
        coEvery { getNewestMessagesUseCase.invoke(stream, topic) } returns flow {
            ResultWrapper.Success(
                listOf<Message>()
            )
        }

        return ChatExecutor(
            dateUiMapper = dateUiMapper,
            messageResponseUiMapper = messageResponseUiMapper,
            getNewestMessagesUseCase = getNewestMessagesUseCase,
            addReactionUseCase = addReactionUseCase,
            removeReactionUseCase = removeReactionUseCase,
            sendMessageUseCase = sendMessageUseCase,
            loadMoreMessagesUseCase = loadMoreMessagesUseCase
        )
    }

    val chatState = ChatStore.State(
        topicName = topic,
        streamName = stream
    )

    private val callbacks =
        object : Executor.Callbacks<ChatStore.State, ChatStore.Message, Nothing> {
            override val state: ChatStore.State
                get() = chatState

            override fun onMessage(message: ChatStore.Message) {
                //вот тут проверять message?
            }

            override fun onLabel(label: Nothing) {
                TODO("Not yet implemented")
            }
        }
}