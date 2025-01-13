package ru.glebik.tinkoff_fintech.feature.chat.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionResponse(
    @SerialName("emoji_name")
    val emojiName: String?,
    @SerialName("emoji_code")
    val emojiCode: String?,
    @SerialName("reaction_type")
    val reactionType: String?,
    @SerialName("user")
    val user: UserSmallData?,
    @SerialName("user_id")
    val userId: Int?,
)

@Serializable
data class UserSmallData(
    @SerialName("email")
    val email: String?,
    @SerialName("full_name")
    val fullName: String?,
    @SerialName("id")
    val id: Int?,
)