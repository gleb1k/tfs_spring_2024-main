package ru.glebik.core.db.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val parentStreamId: Int,
    val unreadMessagesCount : Int
)