package ru.glebik.tinkoff_fintech.feature.chat.data.model.mapper

import ru.glebik.core.db.api.entity.MessageEntity
import ru.glebik.core.db.api.entity.ReactionEntity
import ru.glebik.core.utils.orEmpty
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.MessageResponse
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.ReactionResponse
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Reaction

fun MessageResponse.toDomain(): Message {
    return Message(
        id = id,
        avatarUrl = avatarUrl.orEmpty(),
        content = content.orEmpty(),
        senderEmail = senderEmail.orEmpty(),
        senderFullName = senderFullName.orEmpty(),
        senderId = senderId.orEmpty(),
        streamName = streamName,
        streamId = streamId,
        topic = topic.orEmpty(),
        timestamp = timestamp.orEmpty(),
        reactions = reactionResponses.toDomainList(id)
    )
}

fun MessageEntity.toDomain(reactions: List<ReactionEntity>): Message =
    Message(
        id = id,
        avatarUrl = avatarUrl,
        content = content,
        senderEmail = senderEmail,
        senderFullName = senderFullName,
        senderId = senderId,
        streamName = streamName,
        streamId = streamId,
        topic = topicName,
        timestamp = timestamp,
        reactions = reactions.toDomainList(id)
    )

fun List<ReactionResponse>?.toDomainList(messageId: Int): List<Reaction> {
    if (this == null) return listOf()

    val reactionsMap = mutableMapOf<Pair<String, String>, ArrayList<Int>>()

    this.forEach {
        val element = reactionsMap[it.emojiName to it.emojiCode]

        if (element != null) {
            reactionsMap[it.emojiName to it.emojiCode]?.add(it.userId ?: 0)
        } else {
            val name = it.emojiName.orEmpty()
            val code = it.emojiCode.orEmpty()
            reactionsMap[name to code] = arrayListOf(it.userId ?: 0)
        }
    }

    val resultReactions = arrayListOf<Reaction>()

    reactionsMap.forEach {
        resultReactions.add(
            Reaction(
                messageId = messageId,
                emojiName = it.key.first,
                emojiCode = it.key.second,
                usersIds = it.value
            )
        )
    }

    return resultReactions
}
