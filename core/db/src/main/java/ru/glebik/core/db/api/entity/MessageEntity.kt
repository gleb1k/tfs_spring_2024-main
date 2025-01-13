package ru.glebik.core.db.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val avatarUrl: String,
    val content: String,
    val senderEmail: String,
    val senderFullName: String,
    val senderId: Int,
    val streamName: String,
    val streamId: Int,
    val topicName: String,
    val timestamp: Int,

    //плохо
    //val reactions: List<ReactionEntity>
    //прочитано не прочитано
    //val flags: List<String>,
)