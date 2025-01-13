package ru.glebik.tinkoff_fintech.feature.channels.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.terrakok.cicerone.Router
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.navigation.toCiceroneScreen
import ru.glebik.core.widget.composable.FintechTextField
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TopicUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsStore
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsViewModel
import ru.glebik.tinkoff_fintech.feature.chat.ChatScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChannelsTabs(
    state: ChannelsStore.State,
    viewModel: ChannelsViewModel,
    pagerState: PagerState,
) {
    val selectedTabIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = FintechTheme.colors.secondary,
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(
                        tabPositions[selectedTabIndex],
                    ),
                    color = FintechTheme.colors.primary
                )
            }
        },
    ) {
        state.tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    scope.launch {
                        viewModel.selectTabPage(index)
                        pagerState.animateScrollToPage(index)
                    }
                },
                content = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(tab.titleResId),
                            modifier = Modifier.padding(8.dp),
                            style = FintechTheme.typography.base.copy(
                                fontSize = 24.sp,
                                color = if (selectedTabIndex == index) {
                                    FintechTheme.colors.white
                                } else {
                                    FintechTheme.colors.grayText
                                }
                            ),
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainContent(
    state: ChannelsStore.State,
    viewModel: ChannelsViewModel,
    pagerState: PagerState,
    router: Router,
) {
    if (state.isLoading) {
        LoadingChannelsList()
    } else {
        HorizontalPager(state = pagerState) {
            when (it) {
                0 -> StreamList(state.subscribedStreams, viewModel, router)
                else -> StreamList(state.allStreams, viewModel, router)
            }
        }
    }
    if (state.dialogState.isShow) {
        DialogCreateChannel(
            onDismiss = { viewModel.onAddChannelDialogVisibleChange(false) },
            onCreate = {
                viewModel.createStream()
                viewModel.onAddChannelDialogVisibleChange(false)
            },
            onQueryChange = viewModel::onAddChannelDialogQueryChange,
            state = state
        )
    }
}

@Composable
internal fun StreamList(
    streams: List<StreamUiModel>,
    viewModel: ChannelsViewModel,
    router: Router,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        streams.forEach { stream ->
            item {
                StreamItem(
                    stream = stream,
                    onChannelClick = { viewModel.onCollapseStream(stream) },
                    router,
                )
            }
            //shimmer for topics
            item {
                repeat(ChannelsFragment.SHIMMER_TOPICS_ITEMS_COUNT) {
                    AnimatedVisibility(
                        visible = stream.isCollapsed && stream.isTopicsLoading,
                        enter = fadeIn(animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)) +
                                expandVertically(
                                    animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)
                                ),
                    ) {
                        TopicLoadingItem()
                    }
                }
            }

            itemsIndexed(
                items = stream.topics
            ) { _, topic ->

                AnimatedVisibility(
                    visible = stream.isCollapsed && !stream.isTopicsLoading,
                    enter = fadeIn(animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)) +
                            expandVertically(
                                animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)
                            ),
                    exit = fadeOut(animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)) +
                            shrinkVertically(
                                animationSpec = tween(ChannelsFragment.ANIMATION_DURATION)
                            )
                ) {
                    TopicItem(topic = topic) {
                        router.navigateTo(
                            ChatScreen(
                                topic.name,
                                stream.name
                            ).toCiceroneScreen(
                                R.animator.fade_in,
                                R.animator.fade_out,
                            )
                        )
                    }
                    Divider(
                        thickness = 1.dp,
                        color = FintechTheme.colors.secondary
                    )
                }
            }

            item {
                Divider(
                    thickness = 1.dp,
                    color = FintechTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun StreamItem(
    stream: StreamUiModel,
    onChannelClick: () -> Unit,
    router: Router,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChannelClick()
            }
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(10f)
        ) {
            IconButton(
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
                onClick = {
                    router.navigateTo(
                        ChatScreen(
                            topicName = null,
                            streamName = stream.name
                        ).toCiceroneScreen(
                            R.animator.fade_in,
                            R.animator.fade_out,
                        )
                    )
                }) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(id = R.string.navigate_to_stream_topics),
                    tint = FintechTheme.colors.grayIcon
                )
            }
            Text(
                stream.name,
                style = FintechTheme.typography.base.copy(
                    fontSize = 22.sp,
                    color = FintechTheme.colors.white
                ),
                modifier = Modifier.padding(vertical = 12.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        val rotation by animateFloatAsState(
            targetValue = if (stream.isCollapsed) 0f else -180f,
            animationSpec = tween(ChannelsFragment.ANIMATION_DURATION),
            label = stringResource(id = R.string.rotation_animation_label)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_up),
            contentDescription = stringResource(id = R.string.content_description_hide),
            tint = if (stream.isCollapsed) FintechTheme.colors.white else FintechTheme.colors.grayIcon,
            modifier = Modifier
                .rotate(rotation)
                .weight(1f)
        )

    }

}

@Composable
private fun TopicLoadingItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .shimmer()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(16.dp)
                .background(
                    FintechTheme.colors.white,
                    shape = FintechTheme.cornerShape.rounded6dp
                )
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(16.dp)
                .background(
                    FintechTheme.colors.white,
                    shape = FintechTheme.cornerShape.rounded6dp
                )
        )
    }
    Divider(
        thickness = 1.dp,
        color = FintechTheme.colors.secondary
    )
}

@Composable
private fun TopicItem(
    topic: TopicUiModel,
    onTopicClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(FintechTheme.colors.primary)
            .clickable {
                onTopicClick()
            }
    ) {
        Text(
            topic.name,
            style = FintechTheme.typography.base.copy(
                fontSize = 22.sp,
                color = FintechTheme.colors.white
            ),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        Text(
            stringResource(id = R.string.topic_unread_messages, topic.messagesCount),
            style = FintechTheme.typography.base.copy(
                fontSize = 20.sp,
                color = FintechTheme.colors.grayText
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun LoadingChannelsList() {
    repeat(ChannelsFragment.SHIMMER_STREAMS_ITEMS_COUNT) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(128.dp)
                    .height(24.dp)
                    .background(
                        FintechTheme.colors.white,
                        shape = FintechTheme.cornerShape.rounded6dp
                    )
            )
        }
        Divider(
            thickness = 1.dp,
            color = FintechTheme.colors.secondary
        )
    }
}

@Composable
fun DialogCreateChannel(
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    onQueryChange: (String) -> Unit,
    state: ChannelsStore.State,
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
                    text = stringResource(id = R.string.creating_channel_title),
                    style = FintechTheme.typography.base.copy(
                        fontSize = 22.sp,
                        color = FintechTheme.colors.white
                    ),
                )

                var hasFocus by remember { mutableStateOf(false) }

                val focusRequester = remember { FocusRequester() }

                FintechTextField(
                    value = state.dialogState.nameQuery,
                    onValueChange = onQueryChange,
                    hint = stringResource(id = R.string.creating_channel_hint),
                    hasFocus = hasFocus,
                    backgroundColor = FintechTheme.colors.secondary,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            hasFocus = it.hasFocus
                        },
                )
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
                        onClick = onCreate,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(
                            stringResource(id = R.string.create_channel),
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