package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import javax.inject.Inject

@FeatureScope
class GetNewestMessagesUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : GetNewestMessagesUseCase {
    override suspend fun invoke(
        streamName: String,
        topicName: String?,
    ): Flow<ResultWrapper<List<Message>>> =
        messageRepository.newestMessagesFlow(streamName, topicName)
}