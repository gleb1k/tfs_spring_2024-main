package ru.glebik.core.widget.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.R

@Composable
fun BackTopBar(
    title: String,
    onBackClick: () -> Unit = { },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(FintechTheme.colors.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        IconButton(
            onClick = onBackClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(
                    id = R.string.back
                ),
                tint = FintechTheme.colors.white
            )
        }

        Text(
            text = title,
            style = FintechTheme.typography.base.copy(
                fontSize = 28.sp,
                color = FintechTheme.colors.white
            ),
            modifier = Modifier.padding(horizontal = 16.dp).testTag("BackTopBarTitle"),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}