package ru.glebik.core.designsystem.theme.values

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


data class Colors(
    val primary: Color = Color(0xFF2A9D8F),
    val secondary: Color = Color(0xff1E1E1E),
    val white: Color = Color(0xFFFAFAFA),
    val error: Color = Color(0xFFDB3223),
    val grayText : Color = Color(0xFFCCCCCC),
    val grayLight: Color = Color(0xFF3A3A3A),
    val grayIcon : Color = Color(0xff919191),

    val greenOnline: Color = Color(0xff4AB54D),
    val orangeIdle: Color = Color(0xffEB7F19),
    val background: Color = Color(0xff121212),
    val grayButton: Color = Color(0xff1B1B1B),
)

data class Typography(
    val base: TextStyle,
)

data class Padding(
    val _4dp: Dp,
    val _8dp: Dp,
    val _12dp: Dp,
    val _16dp: Dp,
    val _24dp: Dp,
    val _32dp: Dp,
)

data class CornerShape(
    val rounded6dp: CornerBasedShape,
    val rounded8dp: CornerBasedShape,
    val rounded12dp: CornerBasedShape,
)