package ru.glebik.tinkoff_fintech.feature.people.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.composable.FintechImage
import ru.glebik.tinkoff_fintech.feature.people.PeopleFragment.Companion.SHIMMER_LOADING_COUNT
import ru.glebik.tinkoff_fintech.feature.user.ui.model.UserUiModel


@Composable
internal fun UserItem(user: UserUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
        ) {
            FintechImage(
                model = user.avatarUrl,
                requestBuilderTransform = { it.circleCrop() },
                modifier = Modifier
                    .fillMaxSize(),
                loadingPlaceholder = {
                    Box(
                        modifier = Modifier
                            .shimmer()
                            .fillMaxSize()
                            .background(
                                color = FintechTheme.colors.white,
                                shape = CircleShape
                            )
                    )
                }
            )

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(user.status.color)
                    .align(Alignment.BottomEnd),
            ) {

            }
        }
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                user.name,
                style = FintechTheme.typography.base.copy(
                    fontSize = 24.sp,
                    color = FintechTheme.colors.white
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                user.email,
                style = FintechTheme.typography.base.copy(
                    fontSize = 20.sp,
                    color = FintechTheme.colors.grayText
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun LoadingPeopleList(
    scaffoldPadding: PaddingValues,
) {

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        repeat(SHIMMER_LOADING_COUNT) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shimmer(shimmerInstance)
                        .size(64.dp)
                        .background(
                            FintechTheme.colors.white,
                            shape = CircleShape
                        )
                ) {
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .shimmer(shimmerInstance)
                            .height(32.dp)
                            .width(128.dp)
                            .background(
                                FintechTheme.colors.white,
                                shape = FintechTheme.cornerShape.rounded6dp
                            )

                    )
                    Spacer(Modifier.padding(4.dp))
                    Box(
                        modifier = Modifier
                            .shimmer(shimmerInstance)
                            .height(24.dp)
                            .width(256.dp)
                            .background(
                                FintechTheme.colors.white,
                                shape = FintechTheme.cornerShape.rounded6dp
                            )
                    )
                }
            }

        }
    }
}