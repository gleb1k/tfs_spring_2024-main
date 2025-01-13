package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message

interface LoadMoreMessagesUseCase {
    suspend operator fun invoke(
        streamName: String,
        topicName: String?,
        lastMessageId: Int,
    ): ResultWrapper<List<Message>>
}