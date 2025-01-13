package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream

interface GetSubscribedStreamsUseCase {
    suspend operator fun invoke(): Flow<ResultWrapper<List<Stream>>>
}