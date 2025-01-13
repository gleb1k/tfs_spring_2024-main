package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import javax.inject.Inject

@FeatureScope
class SendMessageUseCaseImpl @Inject constructor(
    private val repository: MessageRepository,
) : SendMessageUseCase {

    override suspend fun invoke(
        streamName: String,
        topicName: String?,
        content: String,
    ): ResultWrapper<Message> =
        repository.sendMessage(streamName, topicName, content)
}