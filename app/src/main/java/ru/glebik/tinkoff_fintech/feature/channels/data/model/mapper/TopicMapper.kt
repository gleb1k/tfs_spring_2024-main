package ru.glebik.tinkoff_fintech.feature.channels.data.model.mapper

import ru.glebik.core.db.api.entity.TopicEntity
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.TopicResponse
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic

fun TopicResponse.toDomain(unreadMessagesCount: Int) = Topic(
    id = this.maxId,
    name = this.name.orEmpty(),
    unreadMessagesCount = unreadMessagesCount
)

fun TopicEntity.toDomain(): Topic = Topic(
    id = id, name = name, unreadMessagesCount = unreadMessagesCount
)

fun TopicResponse.toEntity(streamId: Int, unreadCount: Int): TopicEntity = TopicEntity(
    name = this.name.orEmpty(), parentStreamId = streamId, unreadMessagesCount = unreadCount
)