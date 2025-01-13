package ru.glebik.tinkoff_fintech.feature.user.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnUserResponse(
    @SerialName("avatar_url")
    override val avatarUrl: String?,
    @SerialName("avatar_version")
    val avatarVersion: Int?,
    @SerialName("date_joined")
    val dateJoined: String?,
    @SerialName("delivery_email")
    val deliveryEmail: String?,
    @SerialName("email")
    override val email: String?,
    @SerialName("full_name")
    override val fullName: String?,
    @SerialName("is_active")
    val isActive: Boolean?,
    @SerialName("is_admin")
    val isAdmin: Boolean?,
    @SerialName("is_billing_admin")
    val isBillingAdmin: Boolean?,
    @SerialName("is_bot")
    val isBot: Boolean?,
    @SerialName("is_guest")
    val isGuest: Boolean?,
    @SerialName("is_owner")
    val isOwner: Boolean?,
    @SerialName("max_message_id")
    val maxMessageId: Int?,
    @SerialName("msg")
    val msg: String?,
    @SerialName("result")
    val result: String?,
    @SerialName("role")
    val role: Int?,
    @SerialName("timezone")
    val timezone: String?,
    @SerialName("user_id")
    override val userId: Int,
) : BaseUserResponse