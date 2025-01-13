package ru.glebik.tinkoff_fintech.feature.channels.data.model.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class StreamsResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("msg")
    val msg: String?,
    @SerialName("result")
    val result: String?,
    @JsonNames("streams", "subscriptions")
    val streamsResponse: List<StreamResponse>?,
)

@Entity(tableName = "streams")
@Serializable
data class StreamResponse(
    @SerialName("stream_id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val streamId: Int,
    @SerialName("name")
    val name: String?,
)