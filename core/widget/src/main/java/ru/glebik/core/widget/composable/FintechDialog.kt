package ru.glebik.core.widget.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.R

@Composable
fun FintechDialog(
    @StringRes
    titleResId: Int,
    @StringRes
    okButtonResId: Int,
    onDismiss: () -> Unit,
    okAction: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = FintechTheme.colors.secondary
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = titleResId),
                    style = FintechTheme.typography.base.copy(
                        fontSize = 22.sp,
                        color = FintechTheme.colors.white
                    ),
                )

                content()

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(
                            stringResource(id = R.string.cancel),
                            style = FintechTheme.typography.base.copy(
                                fontSize = 18.sp,
                                color = FintechTheme.colors.white
                            )
                        )
                    }
                    TextButton(
                        onClick = okAction,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(
                            stringResource(id = okButtonResId),
                            style = FintechTheme.typography.base.copy(
                                fontSize = 18.sp,
                                color = FintechTheme.colors.primary
                            )
                        )
                    }
                }
            }
        }
    }
}