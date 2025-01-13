package ru.glebik.core.widget.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.glebik.core.designsystem.theme.FintechTheme


@Composable
fun FintechTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    hasFocus: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    backgroundColor: Color = FintechTheme.colors.secondary,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        enabled = enabled,
        textStyle = FintechTheme.typography.base.copy(
            fontSize = 18.sp
        ),
        cursorBrush = SolidColor(FintechTheme.colors.primary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        modifier = modifier
            .background(backgroundColor),
        decorationBox = { innerTextField ->
            Column {
                Box {
                    Text(
                        text = hint,
                        style = FintechTheme.typography.base.copy(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .alpha(if (value.isEmpty()) 1f else 0f)
                            .padding(top = 2.dp)
                    )
                    innerTextField()
                }

                Divider(
                    color = if (hasFocus) FintechTheme.colors.primary
                    else FintechTheme.colors.grayText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
    )
}