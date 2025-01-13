package ru.glebik.tinkoff_fintech.feature.user.domain.model

data class User(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val status: UserStatus,
    val email: String,
)

enum class UserStatus {
    ONLINE, IDLE, OFFLINE
}


