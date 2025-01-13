package ru.glebik.tinkoff_fintech.feature.channels.ui.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import ru.glebik.core.presentation.mvi.MviViewModel
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel

class ChannelsViewModel(
    private val store: ChannelsStore,
    @DefaultDispatcherQualifier
    private val defaultDispatcher: CoroutineDispatcher,
) : MviViewModel<ChannelsStore.Intent, ChannelsStore.State, ChannelsStore.Label>(
    store
) {

    private val searchQueryPublisher = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        loadSubscribed()
        loadAllStreams()

        listenToSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenToSearchQuery() {
        searchQueryPublisher
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(CHANNELS_SEARCH_DEBOUNCE)
            .filter { it != state.value.lastSearchedQuery }
            .flowOn(defaultDispatcher)
            .mapLatest(::searchStreams)
            .launchIn(viewModelScope)
    }

    fun createStream() {
        store.accept(ChannelsStore.Intent.CreateStream(state.value.dialogState.nameQuery))
    }

    fun onAddChannelDialogVisibleChange(isShow: Boolean) =
        store.accept(ChannelsStore.Intent.OnShowAddChannelDialog(isShow))

    fun onAddChannelDialogQueryChange(newValue: String) =
        store.accept(ChannelsStore.Intent.OnAddChannelDialogQueryChange(newValue))

    fun searchStreams(query: String) =
        when (state.value.selectedPage) {
            ChannelsStore.ChannelTabPage.SUBSCRIBED -> store.accept(
                ChannelsStore.Intent.SearchSubscribedStreams(
                    query
                )
            )

            ChannelsStore.ChannelTabPage.ALL_STREAMS -> store.accept(
                ChannelsStore.Intent.SearchAllStreams(
                    query
                )
            )
        }

    fun selectTabPage(page: Int) = store.accept(ChannelsStore.Intent.ChangeTabPage(page))

    fun onSearchQueryChange(newValue: String) {
        store.accept(ChannelsStore.Intent.OnSearchQueryChange(newValue))
        searchQueryPublisher.tryEmit(newValue)
    }

    fun onSearchVisibleChange() =
        store.accept(ChannelsStore.Intent.OnSearchVisibleChange(!state.value.isSearchVisible))

    fun onCollapseStream(stream: StreamUiModel) {
        store.accept(ChannelsStore.Intent.OnCollapseModeChange(stream))

        if (!stream.isCollapsed)
            store.accept(ChannelsStore.Intent.LoadStreamTopics(stream))
    }

    fun loadSubscribed() = store.accept(ChannelsStore.Intent.LoadSubscribedStreams)

    fun loadAllStreams() = store.accept(ChannelsStore.Intent.LoadAllStreams)

    companion object {
        private const val CHANNELS_SEARCH_DEBOUNCE = 500L

        fun provideFactory(
            store: ChannelsStore,
            defaultDispatcher: CoroutineDispatcher,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ChannelsViewModel(
                    store, defaultDispatcher
                )
            }
        }
    }
}