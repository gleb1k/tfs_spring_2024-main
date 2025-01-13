package ru.glebik.tinkoff_fintech.feature.chat.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.composable.FintechDialog
import ru.glebik.core.widget.composable.FintechTextField
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatViewModel


@Composable
fun ChatDialog(
    state: ChatStore.State,
    viewModel: ChatViewModel,
) {
    if (state.dialogState.isShow) {
        when (state.dialogState.type) {
            ChatStore.ChatDialogState.Type.EDIT_CONTENT -> {
                EditMessageContentDialog(
                    onDismiss = viewModel::hideChatDialog,
                    onEditClick = viewModel::editMessageContentClick,
                    textFieldQuery = state.dialogState.query,
                    onQueryChange = viewModel::onChatDialogQueryChange
                )
            }

            ChatStore.ChatDialogState.Type.EDIT_TOPIC -> {
                EditMessageTopicDialog(
                    onDismiss = viewModel::hideChatDialog,
                    onEditClick = viewModel::editMessageTopicClick,
                    textFieldQuery = state.dialogState.query,
                    onQueryChange = viewModel::onChatDialogQueryChange
                )
            }
        }
    }
}

@Composable
fun EditMessageContentDialog(
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    textFieldQuery: String,
    onQueryChange: (String) -> Unit,
) {
    FintechDialog(
        titleResId = R.string.chat_dialog_edit_message_content_title,
        okButtonResId = R.string.chat_dialog_edit,
        onDismiss = onDismiss,
        okAction = onEditClick
    ) {

        var hasFocus by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        FintechTextField(
            value = textFieldQuery,
            onValueChange = onQueryChange,
            hint = stringResource(id = R.string.chat_dialog_edit_message_content_placeholder),
            hasFocus = hasFocus,
            backgroundColor = FintechTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    hasFocus = it.hasFocus
                },
        )
    }
}

@Composable
fun EditMessageTopicDialog(
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    textFieldQuery: String,
    onQueryChange: (String) -> Unit,
) {
    FintechDialog(
        titleResId = R.string.chat_dialog_change_message_topic_title,
        okButtonResId = R.string.chat_dialog_change,
        onDismiss = onDismiss,
        okAction = onEditClick
    ) {

        var hasFocus by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        FintechTextField(
            value = textFieldQuery,
            onValueChange = onQueryChange,
            hint = stringResource(id = R.string.chat_dialog_edit_message_topic_placeholder),
            hasFocus = hasFocus,
            backgroundColor = FintechTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    hasFocus = it.hasFocus
                },
        )
    }
}