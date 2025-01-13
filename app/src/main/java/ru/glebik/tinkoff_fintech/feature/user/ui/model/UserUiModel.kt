package ru.glebik.tinkoff_fintech.feature.user.ui.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import ru.glebik.tinkoff_fintech.R

data class UserUiModel(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val status: UserUiStatus,
    val email: String,
)

enum class UserUiStatus(val color: Color, @StringRes val textRes: Int) {
    ONLINE(Color(0xff4AB54D), R.string.user_status_online),
    IDLE(Color(0xffEB7F19), R.string.user_status_idle),
    OFFLINE(Color(0xFFDB3223), R.string.user_status_offline)
}
