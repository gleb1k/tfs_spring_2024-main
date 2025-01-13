package ru.glebik.tinkoff_fintech.feature.chat.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.chat.data.ChatLongPollService
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepositoryImpl
import ru.glebik.tinkoff_fintech.feature.chat.data.MessagesApi
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.AddReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.AddReactionUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.DeleteMessageUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.DeleteMessageUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageContentUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageContentUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageTopicUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.EditMessageTopicUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.GetNewestMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.GetNewestMessagesUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.LoadMoreMessagesUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.LoadMoreMessagesUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.RemoveReactionUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.RemoveReactionUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.SendMessageUseCase
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.SendMessageUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.mapper.MessageUiMapper

@Module
interface ChatModule {

    @Binds
    fun bindMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository

    @Binds
    fun bindGetMessagesUseCase(
        getMessagesUseCaseImpl: GetNewestMessagesUseCaseImpl,
    ): GetNewestMessagesUseCase

    @Binds
    fun bindAddReactionUseCase(addReactionUseCaseImpl: AddReactionUseCaseImpl): AddReactionUseCase

    @Binds
    fun bindRemoveReactionUseCase(
        removeReactionUseCaseImpl: RemoveReactionUseCaseImpl,
    ): RemoveReactionUseCase

    @Binds
    fun bindSendMessageUseCase(sendMessageUseCaseImpl: SendMessageUseCaseImpl): SendMessageUseCase

    @Binds
    fun bindLoadMoreMessagesUseCase(
        loadMoreMessagesUseCaseImpl: LoadMoreMessagesUseCaseImpl,
    ): LoadMoreMessagesUseCase

    @Binds
    fun bindDeleteMessageUseCase(
        deleteMessageUseCaseImpl: DeleteMessageUseCaseImpl,
    ): DeleteMessageUseCase

    @Binds
    fun bindEditMessageTopicUseCase(
        editMessageTopicUseCaseImpl:
        EditMessageTopicUseCaseImpl,
    ): EditMessageTopicUseCase

    @Binds
    fun bindEditMessageContentUseCase(
        editMessageContentUseCaseImpl: EditMessageContentUseCaseImpl,
    ): EditMessageContentUseCase

    @MessageUiMapperQualifier
    @Binds
    fun bindMessageUiMapper(messageUiMapper: MessageUiMapper): UiMapper<Message, MessageUiModel>

    companion object {

        @Provides
        fun provideMessagesApi(
            retrofit: Retrofit,
        ): MessagesApi = retrofit.create(MessagesApi::class.java)

        @Provides
        fun provideChatLongPollService(
            messagesApi: MessagesApi,
        ): ChatLongPollService =
            ChatLongPollService(
                messagesApi
            )
    }

}