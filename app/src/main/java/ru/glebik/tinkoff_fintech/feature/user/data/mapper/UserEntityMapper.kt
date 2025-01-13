package ru.glebik.tinkoff_fintech.feature.user.data.mapper

import ru.glebik.core.db.api.entity.UserEntity
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import ru.glebik.tinkoff_fintech.feature.user.domain.model.UserStatus

fun UserEntity.toDomain(): User {
    return User(
        id = id, name = name,
        avatarUrl = avatarUrl,
        status = UserStatus.entries[status],
        email = email
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        status = status.ordinal,
        email = email
    )
}