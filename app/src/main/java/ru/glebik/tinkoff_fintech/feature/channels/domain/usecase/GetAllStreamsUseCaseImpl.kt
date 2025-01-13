package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepository
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import javax.inject.Inject

@FeatureScope
class GetAllStreamsUseCaseImpl @Inject constructor(
    private val repository: ChannelsRepository,
) : GetAllStreamsUseCase {
    override suspend fun invoke(): Flow<ResultWrapper<List<Stream>>> = repository.getAllStreams()
}