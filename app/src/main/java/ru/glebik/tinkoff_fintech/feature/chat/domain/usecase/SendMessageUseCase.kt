package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message


interface SendMessageUseCase {

    suspend operator fun invoke(
        streamName: String,
        topicName: String?,
        content: String,
    ): ResultWrapper<Message>

}