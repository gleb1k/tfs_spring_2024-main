package ru.glebik.core.widget.composable

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.integration.compose.RequestState
import com.valentinilk.shimmer.shimmer
import ru.glebik.core.designsystem.theme.FintechTheme


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FintechImage(
    model: Any,
    modifier: Modifier = Modifier,
    requestBuilderTransform: RequestBuilderTransform<Drawable> = { it },
    loadingPlaceholder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .shimmer()
                .fillMaxSize()
                .background(
                    color = FintechTheme.colors.white,
                    shape = FintechTheme.cornerShape.rounded12dp
                )
        )
    },
    errorPlaceholder: @Composable () -> Unit = {},
) {
    GlideSubcomposition(
        model = model,
        requestBuilderTransform = requestBuilderTransform,
        modifier = modifier
    ) {
        when (state) {
            RequestState.Failure -> errorPlaceholder()
            RequestState.Loading -> loadingPlaceholder()
            is RequestState.Success -> Image(painter, contentDescription = null)
        }
    }
}
