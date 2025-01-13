package ru.glebik.core.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseInfoResponse(
    @SerialName("msg")
    val msg: String?,
    @SerialName("result")
    val result: String?
)