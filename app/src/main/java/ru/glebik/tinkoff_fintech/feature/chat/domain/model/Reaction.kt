package ru.glebik.tinkoff_fintech.feature.chat.domain.model

data class Reaction(
    val messageId: Int,
    val emojiName: String,
    val emojiCode: String,
    val usersIds: List<Int>,
)
