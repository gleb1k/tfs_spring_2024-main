package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepository
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import javax.inject.Inject

@FeatureScope
class GetStreamTopicsUseCaseImpl @Inject constructor(
    private val repository: ChannelsRepository,
) : GetStreamTopicsUseCase {
    override suspend fun invoke(streamId: Int): Flow<ResultWrapper<List<Topic>>> =
        repository.getStreamTopics(streamId)
}