package ru.glebik.tinkoff_fintech.feature.user.data.model

import kotlinx.serialization.SerialName

interface BaseUserResponse {
    @SerialName("user_id")
    val userId: Int?

    @SerialName("full_name")
    val fullName: String?

    @SerialName("avatar_url")
    val avatarUrl: String?

    @SerialName("email")
    val email: String?
}
