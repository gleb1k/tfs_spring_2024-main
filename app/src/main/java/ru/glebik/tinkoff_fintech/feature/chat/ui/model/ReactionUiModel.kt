package ru.glebik.tinkoff_fintech.feature.chat.ui.model

data class ReactionUiModel(
    val messageId: Int,
    val emojiName: String,
    val emojiCode: String,
    val usersIds: List<Int>,
    val isSelected: Boolean,
)
