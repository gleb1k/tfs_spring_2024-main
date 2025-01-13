package ru.glebik.tinkoff_fintech.feature.channels.data.model.mapper

import ru.glebik.core.db.api.entity.StreamEntity
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.StreamResponse
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream

fun StreamResponse.toDomain(): Stream = Stream(
    id = this.streamId, name = this.name.orEmpty(), topics = null
)

fun StreamEntity.toDomain(): Stream = Stream(
    id = id, name = name, topics = null
)

fun StreamResponse.toEntity(isSubscribed: Boolean): StreamEntity = StreamEntity(
    id = streamId, name = name.orEmpty(), isSubscribed = isSubscribed
)

