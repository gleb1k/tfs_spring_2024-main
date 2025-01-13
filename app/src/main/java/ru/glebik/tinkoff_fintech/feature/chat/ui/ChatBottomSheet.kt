package ru.glebik.tinkoff_fintech.feature.chat.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.utils.orEmpty
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.data.model.EmojiNCS
import ru.glebik.tinkoff_fintech.feature.chat.data.model.emojis
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatBottomSheet(
    state: ChatStore.State,
    viewModel: ChatViewModel,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    if (state.bottomSheetState.isShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = FintechTheme.colors.secondary,
        ) {
            val clipboardManager = LocalClipboardManager.current

            when (state.bottomSheetState.type) {
                ChatStore.ChatBottomSheetState.Type.LIST -> {
                    Column(
                        modifier = Modifier
                    ) {
                        BottomSheetListItem(
                            R.drawable.ic_emoji,
                            R.string.chat_bottom_sheet_add_reaction
                        ) {
                            viewModel.changeEmojiSheetType(ChatStore.ChatBottomSheetState.Type.REACTIONS)
                        }
                        BottomSheetListItem(
                            R.drawable.ic_copy,
                            R.string.chat_bottom_sheet_copy_message
                        ) {
                            clipboardManager.setText(
                                viewModel.getMessageContentById(
                                    state.bottomSheetState.messageId.orEmpty()
                                )
                            )
                            onDismiss()
                        }
                        BottomSheetListItem(
                            R.drawable.ic_edit,
                            R.string.chat_bottom_sheet_edit_message
                        ) {
                            viewModel.onShowEditContentChatDialog(
                                state.bottomSheetState.messageId.orEmpty()
                            )
                            onDismiss()
                        }
                        BottomSheetListItem(
                            R.drawable.ic_edit_topic,
                            R.string.chat_bottom_sheet_change_topic_message
                        ) {
                            viewModel.onShowChangeTopicChatDialog(
                                state.bottomSheetState.messageId.orEmpty(),
                            )
                            onDismiss()
                        }
                        BottomSheetListItem(
                            R.drawable.ic_delete,
                            R.string.chat_bottom_sheet_delete_message
                        ) {
                            viewModel.deleteMessage()
                            onDismiss()
                        }
                        Spacer(modifier = Modifier.padding(24.dp))
                    }
                }

                ChatStore.ChatBottomSheetState.Type.REACTIONS -> {
                    EmojiList(
                        bottomSheetState = state.bottomSheetState,
                        emojis = emojis,
                        onEmojiClick = viewModel::addReaction
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSheetListItem(
    @DrawableRes
    iconResId: Int,
    @StringRes
    titleResId: Int,
    onItemClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            }
            .padding(horizontal = 24.dp)
    ) {
        Icon(
            painterResource(id = iconResId),
            stringResource(id = titleResId),
            tint = FintechTheme.colors.grayIcon,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = stringResource(id = titleResId),
            style = FintechTheme.typography.base.copy(
                fontSize = 20.sp,
                color = FintechTheme.colors.white
            ),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun EmojiList(
    bottomSheetState: ChatStore.ChatBottomSheetState,
    emojis: List<EmojiNCS>,
    onEmojiClick: (String, Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 48.dp),
        modifier = Modifier
            .padding(bottom = 48.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        items(
            emojis,
            key = { emoji -> emoji.code }) {
            Text(text = it.code, fontSize = 40.sp,
                modifier = Modifier.clickable {
                    onEmojiClick(
                        it.name,
                        bottomSheetState.messageId.orEmpty()
                    )

                })
        }
    }
}
