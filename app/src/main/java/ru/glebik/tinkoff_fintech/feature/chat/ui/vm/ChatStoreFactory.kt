package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.chat.di.MessageUiMapperQualifier
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
import javax.inject.Inject

@FeatureScope
class ChatStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @MessageUiMapperQualifier
    private val messageResponseUiMapper: UiMapper<Message, MessageUiModel>,
    private val getNewestMessagesUseCase: GetNewestMessagesUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageContentUseCase: EditMessageContentUseCase,
    private val editMessageTopicUseCase: EditMessageTopicUseCase,
) {
    fun create(topicName: String?, streamName: String): ChatStore = object :
        ChatStore,
        Store<ChatStore.Intent, ChatStore.State,  ChatStore.Label> by storeFactory.create(
            name = ChatStore::class.simpleName,
            initialState = ChatStore.State(topicName = topicName, streamName = streamName),
            bootstrapper = null,
            executorFactory = {
                ChatExecutor(
                    messageResponseUiMapper = messageResponseUiMapper,
                    getNewestMessagesUseCase = getNewestMessagesUseCase,
                    addReactionUseCase = addReactionUseCase,
                    removeReactionUseCase = removeReactionUseCase,
                    sendMessageUseCase = sendMessageUseCase,
                    loadMoreMessagesUseCase = loadMoreMessagesUseCase,
                    deleteMessageUseCase = deleteMessageUseCase,
                    editMessageContentUseCase = editMessageContentUseCase,
                    editMessageTopicUseCase = editMessageTopicUseCase
                )
            },
            reducer = ChatReducer(),
        ) {}
}
