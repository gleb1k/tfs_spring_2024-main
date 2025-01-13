package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message

interface EditMessageTopicUseCase {
    suspend operator fun invoke(
        messageId: Int,
        newTopicName: String,
    ): ResultWrapper<Message>
}