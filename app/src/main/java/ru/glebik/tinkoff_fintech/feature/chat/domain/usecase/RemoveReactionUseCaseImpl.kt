package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import javax.inject.Inject

@FeatureScope
class RemoveReactionUseCaseImpl @Inject constructor(
    private val repository: MessageRepository,
) : RemoveReactionUseCase {
    override suspend fun invoke(messageId: Int, emojiName: String): ResultWrapper<Message> =
        repository.removeReaction(messageId, emojiName)
}