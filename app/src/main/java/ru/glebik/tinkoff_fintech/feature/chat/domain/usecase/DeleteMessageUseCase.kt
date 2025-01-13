package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.network.response.BaseInfoResponse
import ru.glebik.core.utils.ResultWrapper

interface DeleteMessageUseCase {
    suspend operator fun invoke(messageId: Int) : ResultWrapper<BaseInfoResponse>
}