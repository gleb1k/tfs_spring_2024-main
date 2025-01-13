package ru.glebik.tinkoff_fintech.feature.chat.domain.model.mapper

import ru.glebik.core.db.api.entity.MessageEntity
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message

fun Message.toEntity(): MessageEntity =
    MessageEntity(
        id = id,
        avatarUrl = avatarUrl,
        content = content,
        senderEmail = senderEmail,
        senderFullName = senderFullName,
        senderId = senderId,
        streamName = streamName,
        streamId = streamId,
        topicName = topic,
        timestamp = timestamp
    )
