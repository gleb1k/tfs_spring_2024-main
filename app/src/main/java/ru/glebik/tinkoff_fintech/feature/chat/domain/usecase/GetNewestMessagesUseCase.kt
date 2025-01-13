package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message

interface GetNewestMessagesUseCase {

    suspend operator fun invoke(
        streamName: String,
        topicName: String?,
    ): Flow<ResultWrapper<List<Message>>>

}