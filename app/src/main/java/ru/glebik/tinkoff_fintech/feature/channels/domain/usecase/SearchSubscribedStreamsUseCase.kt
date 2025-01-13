package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream

interface SearchSubscribedStreamsUseCase {
    suspend operator fun invoke(name: String): ResultWrapper<List<Stream>>
}