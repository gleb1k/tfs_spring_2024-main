package ru.glebik.tinkoff_fintech.feature.chat.ui.model.mapper

import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.chat.data.model.EmojiNCS
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Reaction
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageDateAndTime
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel
import ru.glebik.tinkoff_fintech.feature.user.data.UserRepositoryImpl
import javax.inject.Inject

class MessageUiMapper @Inject constructor() : UiMapper<Message, MessageUiModel> {
    override fun toDomain(ui: MessageUiModel): Message {
        TODO("Not yet implemented")
    }

    override fun toUi(domain: Message): MessageUiModel =
        MessageUiModel(
            id = domain.id,
            content = domain.content,
            senderEmail = domain.senderEmail,
            senderFullName = domain.senderFullName,
            senderId = domain.senderId,
            avatarUrl = domain.avatarUrl,
            streamName = domain.streamName,
            streamId = domain.streamId,
            timestamp = domain.timestamp,
            topic = domain.topic,
            reactions = domain.reactions.map { it.toUi() }
                .filter { it.emojiCode != EmojiNCS.BAD_EMOJI_CODE },
            isMeMessage = domain.senderEmail == UserRepositoryImpl.CURRENT_USER_EMAIL,
            dateAndTime = MessageDateAndTime.getByTimestamp(timestamp = domain.timestamp)
        )

    private fun Reaction.toUi(): ReactionUiModel =
        ReactionUiModel(
            messageId = messageId,
            emojiName = emojiName,
            emojiCode = EmojiNCS.getCodeString(emojiCode),
            usersIds = usersIds,
            isSelected = usersIds.contains(UserRepositoryImpl.CURRENT_USER_ID)
        )
}

