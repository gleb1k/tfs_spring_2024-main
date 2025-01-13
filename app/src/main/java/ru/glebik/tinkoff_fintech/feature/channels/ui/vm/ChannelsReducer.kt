package ru.glebik.tinkoff_fintech.feature.channels.ui.vm

import com.arkivanov.mvikotlin.core.store.Reducer

class ChannelsReducer : Reducer<ChannelsStore.State, ChannelsStore.Message> {

    override fun ChannelsStore.State.reduce(
        msg: ChannelsStore.Message,
    ) = when (msg) {
        is ChannelsStore.Message.SetError -> copy(
            errorMessage = msg.error.errorMessage,
            isLoading = false,
        )

        ChannelsStore.Message.SetLoading -> copy(
            isLoading = true,
            errorMessage = null,
        )

        is ChannelsStore.Message.SetSearchQuery -> copy(
            searchQuery = msg.query
        )

        is ChannelsStore.Message.SetSearchVisible -> copy(
            isSearchVisible = msg.isVisible
        )

        is ChannelsStore.Message.SetLastSearchedQuery -> copy(
            lastSearchedQuery = msg.searchedQuery
        )

        is ChannelsStore.Message.SelectTabPage -> copy(
            selectedPage = ChannelsStore.ChannelTabPage.entries[msg.pageIndex]
        )

        is ChannelsStore.Message.SetAllStreams -> copy(
            isLoading = false,
            errorMessage = null,
            allStreams = msg.streams
        )

        is ChannelsStore.Message.SetSubscribedStreams -> copy(
            isLoading = false,
            errorMessage = null,
            subscribedStreams = msg.streams
        )

        is ChannelsStore.Message.SetAddChannelDialogQuery -> copy(
            dialogState = ChannelsStore.AddChannelDialogState(nameQuery = msg.query, isShow = true)
        )

        is ChannelsStore.Message.SetAddChannelDialogVisibility -> copy(
            dialogState = ChannelsStore.AddChannelDialogState(nameQuery = "", isShow = msg.isShow)
        )
    }
}