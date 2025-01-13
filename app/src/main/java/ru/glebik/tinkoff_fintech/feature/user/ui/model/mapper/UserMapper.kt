package ru.glebik.tinkoff_fintech.feature.user.ui.model.mapper

import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import ru.glebik.tinkoff_fintech.feature.user.domain.model.UserStatus
import ru.glebik.tinkoff_fintech.feature.user.ui.model.UserUiModel
import ru.glebik.tinkoff_fintech.feature.user.ui.model.UserUiStatus

fun User.toUi(): UserUiModel {

    val status = when (this.status) {
        UserStatus.ONLINE -> UserUiStatus.ONLINE
        UserStatus.IDLE -> UserUiStatus.IDLE
        UserStatus.OFFLINE -> UserUiStatus.OFFLINE
    }

    return UserUiModel(
        id = id, name = name, avatarUrl = avatarUrl, status = status, email = email
    )
}