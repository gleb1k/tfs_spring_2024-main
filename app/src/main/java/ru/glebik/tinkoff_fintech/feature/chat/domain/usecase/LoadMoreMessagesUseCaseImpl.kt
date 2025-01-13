package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import javax.inject.Inject

@FeatureScope
class LoadMoreMessagesUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : LoadMoreMessagesUseCase {
    override suspend fun invoke(
        streamName: String,
        topicName: String?,
        lastMessageId: Int,
    ): ResultWrapper<List<Message>> =
        messageRepository.loadMoreMessages(streamName, topicName, lastMessageId)
}