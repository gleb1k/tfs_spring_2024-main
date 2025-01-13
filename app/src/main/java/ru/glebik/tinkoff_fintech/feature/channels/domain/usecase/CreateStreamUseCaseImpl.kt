package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepository
import ru.glebik.tinkoff_fintech.feature.channels.data.model.SubscriptionsParams
import javax.inject.Inject

@FeatureScope
class CreateStreamUseCaseImpl @Inject constructor(
    private val repository: ChannelsRepository,
) : CreateStreamUseCase {
    override suspend fun invoke(name: String, description: String): ResultWrapper<String> {
        val param = SubscriptionsParams.getWithOneParam(name, description)
        return repository.createStream(param)
    }
}