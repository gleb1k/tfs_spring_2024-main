package ru.glebik.core.widget.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.R
import ru.glebik.core.widget.asBgColor

@Composable
fun ErrorScreen(
    message: String,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .asBgColor(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = FintechTheme.typography.base.copy(
                color = FintechTheme.colors.error,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(onClick = onRetryClick,
             colors = ButtonDefaults.buttonColors(
                 containerColor = FintechTheme.colors.grayButton
             )) {
            Text(
                text = stringResource(id = R.string.retry),
                style = FintechTheme.typography.base.copy(
                    color = FintechTheme.colors.white,
                    fontSize = 16.sp
                )
            )
        }
    }
}