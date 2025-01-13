package ru.glebik.tinkoff_fintech.feature.people.ui.vm

import com.arkivanov.mvikotlin.core.store.Reducer

class PeopleReducer : Reducer<PeopleStore.State, PeopleStore.Message> {

    override fun PeopleStore.State.reduce(
        msg: PeopleStore.Message,
    ) = when (msg) {
        is PeopleStore.Message.SetError -> copy(
            errorMessage = msg.error.errorMessage,
            isLoading = false,
        )

        PeopleStore.Message.SetLoading -> copy(
            isLoading = true,
            errorMessage = null,
        )

        is PeopleStore.Message.SetUsers -> copy(
            users = msg.users,
            errorMessage = null,
            isLoading = false,
        )

        is PeopleStore.Message.SetSearchQuery -> copy(
            searchQuery = msg.query
        )

        is PeopleStore.Message.SetSearchVisible -> copy(
            isSearchVisible = msg.isVisible
        )

        is PeopleStore.Message.SetLastSearchedQuery -> copy(
            lastSearchedQuery = msg.searchedQuery
        )
    }
}