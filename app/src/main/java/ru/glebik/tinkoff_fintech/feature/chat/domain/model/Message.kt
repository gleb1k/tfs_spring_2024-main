package ru.glebik.tinkoff_fintech.feature.chat.domain.model

data class Message(
    val id: Int,
    val avatarUrl: String,
    val content: String,
    val senderEmail: String,
    val senderFullName: String,
    val senderId: Int,
    val streamName: String,
    val streamId: Int,
    val topic: String,
    val timestamp: Int,
    val reactions: List<Reaction>,
)


