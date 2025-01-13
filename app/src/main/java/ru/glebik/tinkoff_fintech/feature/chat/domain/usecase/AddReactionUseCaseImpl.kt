package ru.glebik.tinkoff_fintech.feature.chat.domain.usecase

import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import javax.inject.Inject

@FeatureScope
class AddReactionUseCaseImpl @Inject constructor(
    private val repository: MessageRepository,
) : AddReactionUseCase {
    override suspend fun invoke(messageId: Int, emojiName: String) =
        repository.addReaction(messageId, emojiName)
}