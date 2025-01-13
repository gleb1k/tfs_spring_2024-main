package ru.glebik.tinkoff_fintech.feature.channels.domain.model

data class Topic(
    val id: Int?,
    val name: String,
    val unreadMessagesCount: Int,
)