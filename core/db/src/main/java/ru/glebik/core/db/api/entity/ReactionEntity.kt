package ru.glebik.core.db.api.entity

import androidx.room.Entity

@Entity(tableName = "reactions", primaryKeys = ["messageId", "emojiName"])
data class ReactionEntity(
    val messageId: Int,
    val emojiName: String,
    val emojiCode: String,
    val reactionType: String,
    val userEmail: String,
    val userFullName: String,
    val userId: Int
)