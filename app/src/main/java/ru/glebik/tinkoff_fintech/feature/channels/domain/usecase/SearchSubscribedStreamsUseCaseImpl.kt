package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepository
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import javax.inject.Inject

@FeatureScope
class SearchSubscribedStreamsUseCaseImpl @Inject constructor(
    private val repository: ChannelsRepository,
) : SearchSubscribedStreamsUseCase {
    override suspend fun invoke(name: String): ResultWrapper<List<Stream>> =
        repository.searchSubscribed(name)
}