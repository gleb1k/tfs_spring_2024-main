package ru.glebik.tinkoff_fintech.feature.chat.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.terrakok.cicerone.Router
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.asBgColor
import ru.glebik.core.widget.composable.BackTopBar
import ru.glebik.core.widget.composable.ErrorScreen
import ru.glebik.core.widget.composable.FintechTextField
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ChatFragment
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatViewModel

@Composable
internal fun LoadingChatList() {

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .asBgColor()
    ) {
        repeat(ChatFragment.SHIMMER_ITEMS_COUNT) {
            Row(
                verticalAlignment = Alignment.Top,
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
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Box(
                    modifier = Modifier
                        .shimmer(shimmerInstance)
                        .height(128.dp)
                        .fillMaxWidth()
                        .background(
                            FintechTheme.colors.white,
                            shape = FintechTheme.cornerShape.rounded12dp
                        )

                )
            }
        }
    }
}

@Composable
internal fun ChatTextField(
    state: ChatStore.State,
    viewModel: ChatViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = state.chatQuery,
            onValueChange = viewModel::onChatQueryChange,
            shape = RoundedCornerShape(32.dp),
            singleLine = false,
            trailingIcon = {
                if (state.chatQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onChatQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(id = R.string.chat_clear_text)
                        )
                    }
                }
            },
            placeholder = { Text(text = stringResource(R.string.chat_textfield_placeholder)) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = FintechTheme.colors.grayText,
                unfocusedTextColor = FintechTheme.colors.grayText,
                focusedContainerColor = FintechTheme.colors.grayLight,
                unfocusedContainerColor = FintechTheme.colors.grayLight,
                focusedTrailingIconColor = FintechTheme.colors.grayText,
                focusedIndicatorColor = FintechTheme.colors.primary,
                cursorColor = FintechTheme.colors.primary,
                focusedPlaceholderColor = FintechTheme.colors.grayText,
                unfocusedPlaceholderColor = FintechTheme.colors.grayText,
                disabledContainerColor = FintechTheme.colors.grayLight,
            ),
            modifier = Modifier
                .weight(0.85f)
                .testTag("ChatTextField")
        )
        if (state.chatQuery.isNotEmpty()) {
            ChatTextFieldIcon(
                onClick = viewModel::sendMessage,
                icon = R.drawable.ic_chat_send,
                contentDescription = stringResource(id = R.string.chat_textfield_send),
                modifier = Modifier
                    .weight(0.15f)
                    .testTag("SendMessageIcon")
            )
        } else {
            ChatTextFieldIcon(
                onClick = { },
                icon = R.drawable.ic_chat_plus,
                contentDescription = stringResource(id = R.string.chat_textfield_add_photo),
                modifier = Modifier
                    .weight(0.15f)
                    .testTag("AddPhotoIcon")
            )
        }
    }
}

@Composable
internal fun ChatTextFieldIcon(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier.padding(start = 4.dp)
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = FintechTheme.colors.primary
        )
    }
}


@Composable
internal fun BottomChatComposeView(
    viewModel: ChatViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .asBgColor()
    ) {
        if (state.topicName == null) {
            TopicTextField(state, viewModel)
        }
        ChatTextField(state, viewModel)
    }

    ChatDialog(
        state = state,
        viewModel = viewModel
    )

    ChatBottomSheet(
        state = state,
        viewModel = viewModel,
        onDismiss = viewModel::hideBottomSheet
    )
}

@Composable
internal fun TopicTextField(
    state: ChatStore.State,
    viewModel: ChatViewModel,
) {

    var hasFocus by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    FintechTextField(
        value = state.topicQuery,
        onValueChange = viewModel::onTopicQueryChange,
        hint = stringResource(id = R.string.topic_textfield_placeholder),
        hasFocus = hasFocus,
        backgroundColor = FintechTheme.colors.background,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                hasFocus = it.hasFocus
            },
    )
}

@Composable
internal fun CenterChatComposeView(
    viewModel: ChatViewModel,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingChatList()
    } else {
        if (state.errorMessage != null)
            ErrorScreen(
                state.errorMessage.orEmpty(),
                viewModel::listenToNewestMessages
            )
    }
}

@Composable
internal fun TopChatComposeView(
    viewModel: ChatViewModel,
    router: Router,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(FintechTheme.colors.secondary)
    ) {
        BackTopBar(title = state.streamName) {
            router.exit()
        }
        if (state.topicName != null) {
            Text(
                text = stringResource(id = R.string.topic_prefix) + state.topicName,
                modifier = Modifier
                    .padding(4.dp)
                    .testTag("TextTopic"),
                style = FintechTheme.typography.base.copy(
                    fontSize = 20.sp,
                    color = FintechTheme.colors.grayText
                ),
            )
        }
    }
}