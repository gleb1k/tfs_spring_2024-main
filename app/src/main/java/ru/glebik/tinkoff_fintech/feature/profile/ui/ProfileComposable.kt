package ru.glebik.tinkoff_fintech.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.shimmer
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.composable.FintechImage
import ru.glebik.tinkoff_fintech.feature.profile.ui.vm.ProfileStore

@Composable
internal fun ProfileLoading() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(FintechTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .shimmer()
                .background(
                    FintechTheme.colors.white,
                    shape = FintechTheme.cornerShape.rounded12dp
                )
                .size(200.dp)
        )

        Spacer(Modifier.padding(8.dp))

        Box(
            modifier = Modifier
                .shimmer()
                .width(100.dp)
                .background(
                    FintechTheme.colors.white,
                    shape = FintechTheme.cornerShape.rounded6dp
                )
                .height(32.dp)
        )
    }
}

@Composable
internal fun ProfileScreen(
    state: ProfileStore.State,
) {

    if (state.user != null)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(FintechTheme.colors.background)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FintechImage(
                    model = state.user.avatarUrl, modifier = Modifier
                        .clip(FintechTheme.cornerShape.rounded12dp)
                        .size(200.dp)
                )
                Text(
                    text = state.user.name,
                    style = FintechTheme.typography.base.copy(
                        fontSize = 28.sp,
                        color = FintechTheme.colors.white
                    )
                )

                Text(
                    text = stringResource(id = state.user.status.textRes),
                    style = FintechTheme.typography.base.copy(
                        fontSize = 20.sp,
                        color = state.user.status.color
                    )
                )
            }
        }
}