package ru.glebik.tinkoff_fintech.feature.people.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.glebik.core.presentation.mvi.ScreenState
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.ui.model.UserUiModel

interface PeopleStore : Store<PeopleStore.Intent, PeopleStore.State, PeopleStore.Label> {

    data class State internal constructor(
        val searchQuery: String = "",
        val lastSearchedQuery: String = "",
        val isSearchVisible: Boolean = false,

        val users: PersistentList<UserUiModel> = persistentListOf(),

        override val isLoading: Boolean = false,
        override val errorMessage: String? = null,
    ) : ScreenState

    sealed interface Intent {
        data class OnSearchQueryChange(val query: String) : Intent
        data class OnSearchVisibleChange(val isVisible: Boolean) : Intent

        data object LoadAllUsers : Intent
        data class SearchUsers(val query: String) : Intent
    }

    sealed interface Message {
        data class SetSearchQuery(val query: String) : Message
        data class SetSearchVisible(val isVisible: Boolean) : Message
        data class SetLastSearchedQuery(val searchedQuery: String) : Message

        data class SetUsers(val users: PersistentList<UserUiModel>) : Message

        data object SetLoading : Message
        data class SetError(val error: ResultWrapper.Failed) : Message
    }

    sealed interface Label {
        data class Toast(val message: String) : Label
    }

}