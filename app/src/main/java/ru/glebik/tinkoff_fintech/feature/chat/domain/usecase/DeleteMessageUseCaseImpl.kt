package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.network.response.BaseInfoResponse
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import javax.inject.Inject

@FeatureScope
class DeleteMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : DeleteMessageUseCase {
    override suspend fun invoke(messageId: Int) : ResultWrapper<BaseInfoResponse> =
        messageRepository.deleteMessage(messageId)
}