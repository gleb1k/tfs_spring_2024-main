package ru.glebik.core.db.api.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MessageWithReactions(
    @Embedded val message : MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageId",
    )
    val reactions: List<ReactionEntity>
)