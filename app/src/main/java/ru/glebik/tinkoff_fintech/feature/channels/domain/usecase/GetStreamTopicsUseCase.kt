package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic

interface GetStreamTopicsUseCase {
    suspend operator fun invoke(streamId: Int): Flow<ResultWrapper<List<Topic>>>
}