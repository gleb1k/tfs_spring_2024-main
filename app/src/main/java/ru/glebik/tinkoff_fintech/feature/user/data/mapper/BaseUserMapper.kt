package ru.glebik.tinkoff_fintech.feature.user.data.mapper

import ru.glebik.core.utils.orEmpty
import ru.glebik.tinkoff_fintech.feature.user.data.model.BaseUserResponse
import ru.glebik.tinkoff_fintech.feature.user.data.model.presence.Presence
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import ru.glebik.tinkoff_fintech.feature.user.domain.model.UserStatus

fun BaseUserResponse.mapToUser(
    presence: Presence?,
    serverTimeStamp: Long?,
): User {

    val status = if (serverTimeStamp != null) {
        getUserStatusByTimestamp(
            serverTimeStamp,
            presence?.aggregated?.timestamp.orEmpty().toLong(),
        )
    }else {
        getUserStatus(presence?.aggregated?.status.orEmpty())
    }

    return User(
        id = this.userId.orEmpty(),
        name = this.fullName.orEmpty(),
        avatarUrl = this.avatarUrl.orEmpty(),
        status = status,
        email = this.email.orEmpty()
    )
}

private fun getUserStatus(responseStatus: String): UserStatus {
    return when (responseStatus) {
        "active" -> UserStatus.ONLINE
        "idle" -> UserStatus.IDLE
        else -> UserStatus.OFFLINE
    }
}

private fun getUserStatusByTimestamp(currentTimestamp: Long, userTimestamp: Long): UserStatus {
    val timeDiff = (currentTimestamp - userTimestamp) / 1000
    return when {
        timeDiff <= 300 -> UserStatus.ONLINE //5min
        timeDiff <= 480 -> UserStatus.IDLE  //8min
        else -> UserStatus.OFFLINE
    }
}