package ru.glebik.core.db.api.entity

import androidx.room.Entity

//todo перенести по модулям
//сделал составной primaryKey чтобы не париться с перезаписью подписок/ и всех стримов
@Entity(tableName = "streams", primaryKeys = ["id", "isSubscribed"])
data class StreamEntity(
    val id: Int,
    val name: String,
    val isSubscribed : Boolean,
)