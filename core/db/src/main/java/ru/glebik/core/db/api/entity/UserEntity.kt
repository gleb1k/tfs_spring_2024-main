package ru.glebik.core.db.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val status: Int,
    val email: String,
)
