package ru.glebik.tinkoff_fintech.feature.channels.domain.usecase

import ru.glebik.core.utils.ResultWrapper

interface CreateStreamUseCase {
    suspend operator fun invoke(name: String, description: String = ""): ResultWrapper<String>
}