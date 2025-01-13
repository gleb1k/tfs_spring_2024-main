package ru.glebik.tinkoff_fintech.feature.channels.domain.model

data class Stream(
    val id: Int,
    val name: String,
    val topics: List<Topic>?,
    //muted, notifications, etc...
)

