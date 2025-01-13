package ru.glebik.tinkoff_fintech.feature.people.ui.vm

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
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    private val store: PeopleStore,

    @DefaultDispatcherQualifier
    private val defaultDispatcher: CoroutineDispatcher,
) : MviViewModel<PeopleStore.Intent, PeopleStore.State, PeopleStore.Label>(
    store
) {

    private val searchQueryPublisher =
        MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        loadAllUsers()
        listenToSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenToSearchQuery() {
        searchQueryPublisher
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(PEOPLE_SEARCH_DEBOUNCE)
            .filter { it != state.value.lastSearchedQuery }
            .flowOn(defaultDispatcher)
            .mapLatest(::searchUsers)
            .launchIn(viewModelScope)
    }

    fun loadAllUsers() {
        store.accept(PeopleStore.Intent.LoadAllUsers)
    }

    fun searchUsers(query: String) {
        store.accept(PeopleStore.Intent.SearchUsers(query))
    }

    fun onSearchQueryChange(newValue: String) {
        store.accept(PeopleStore.Intent.OnSearchQueryChange(newValue))
        searchQueryPublisher.tryEmit(newValue)
    }

    fun onSearchVisibleChange() =
        store.accept(PeopleStore.Intent.OnSearchVisibleChange(!state.value.isSearchVisible))

    companion object {
        private const val PEOPLE_SEARCH_DEBOUNCE = 500L

        fun provideFactory(
            store: PeopleStore,
            defaultDispatcher: CoroutineDispatcher,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PeopleViewModel(
                    store, defaultDispatcher
                )
            }
        }
    }
}