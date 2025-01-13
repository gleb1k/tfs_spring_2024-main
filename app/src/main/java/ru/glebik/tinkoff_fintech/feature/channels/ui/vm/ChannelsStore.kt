package ru.glebik.tinkoff_fintech.feature.channels.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.glebik.core.presentation.mvi.ScreenState
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TabItem

interface ChannelsStore : Store<ChannelsStore.Intent, ChannelsStore.State, ChannelsStore.Label> {

    data class State internal constructor(
        val searchQuery: String = "",
        val lastSearchedQuery: String = "",
        val isSearchVisible: Boolean = false,

        val dialogState: AddChannelDialogState = AddChannelDialogState(),

        val subscribedStreams: PersistentList<StreamUiModel> = persistentListOf(),
        val allStreams: PersistentList<StreamUiModel> = persistentListOf(),

        val tabs: PersistentList<TabItem> = persistentListOf(
            TabItem(R.string.tab_subscribed),
            TabItem(R.string.tab_all_streams),
        ),
        val selectedPage: ChannelTabPage = ChannelTabPage.SUBSCRIBED,

        override val isLoading: Boolean = false,
        override val errorMessage: String? = null,
    ) : ScreenState

    data class AddChannelDialogState(
        val nameQuery: String = "",
        val isShow: Boolean = false,
    )

    enum class ChannelTabPage {
        SUBSCRIBED, ALL_STREAMS,
    }

    sealed interface Intent {
        data class OnSearchQueryChange(val query: String) : Intent
        data class OnSearchVisibleChange(val isVisible: Boolean) : Intent
        data class CreateStream(val name: String) : Intent

        data class OnAddChannelDialogQueryChange(val query: String) : Intent
        data class OnShowAddChannelDialog(val isShow: Boolean) : Intent

        data class ChangeTabPage(val pageIndex: Int) : Intent
        data class OnCollapseModeChange(val stream: StreamUiModel) : Intent

        data class LoadStreamTopics(val stream: StreamUiModel) : Intent

        data object LoadAllStreams : Intent
        data object LoadSubscribedStreams : Intent

        data class SearchAllStreams(val query: String) : Intent
        data class SearchSubscribedStreams(val query: String) : Intent
    }

    sealed interface Message {
        data class SetSearchQuery(val query: String) : Message
        data class SetSearchVisible(val isVisible: Boolean) : Message
        data class SetLastSearchedQuery(val searchedQuery: String) : Message

        data class SetAddChannelDialogQuery(val query: String) : Message
        data class SetAddChannelDialogVisibility(val isShow: Boolean) : Message

        data class SetSubscribedStreams(val streams: PersistentList<StreamUiModel>) : Message
        data class SetAllStreams(val streams: PersistentList<StreamUiModel>) : Message

        data class SelectTabPage(val pageIndex: Int) : Message

        data object SetLoading : Message
        data class SetError(val error: ResultWrapper.Failed) : Message
    }

    sealed interface Label {
        data class Toast(val message: String) : Label
    }


}