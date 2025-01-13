package ru.glebik.core.widget

import android.content.Context
import android.util.TypedValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import ru.glebik.core.designsystem.theme.FintechTheme

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun Modifier.asBgColor(): Modifier = composed { this.background(FintechTheme.colors.background) }

fun Modifier.clickableNoInteraction(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}