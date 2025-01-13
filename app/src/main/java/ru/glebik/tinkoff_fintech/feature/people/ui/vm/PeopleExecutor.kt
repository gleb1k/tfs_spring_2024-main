package ru.glebik.tinkoff_fintech.feature.people.ui.vm

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import ru.glebik.core.presentation.mvi.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.parseErrorMessage
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.ui.model.mapper.toUi

class PeopleExecutor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
) : BaseExecutor<PeopleStore.Intent, Unit, PeopleStore.State, PeopleStore.Message, PeopleStore.Label>() {

    private var loadUsersJob: Job? = null

    override fun executeIntent(intent: PeopleStore.Intent, getState: () -> PeopleStore.State) {
        when (intent) {
            PeopleStore.Intent.LoadAllUsers -> {
                if (loadUsersJob?.isActive == true) return

                loadUsersJob = scope.launchSafe {
                    loadAllUsers()
                }
            }

            is PeopleStore.Intent.SearchUsers -> scope.launchSafe {
                searchUsers(intent.query)
            }

            is PeopleStore.Intent.OnSearchQueryChange -> dispatch(
                PeopleStore.Message.SetSearchQuery(intent.query)
            )

            is PeopleStore.Intent.OnSearchVisibleChange -> dispatch(
                PeopleStore.Message.SetSearchVisible(intent.isVisible)
            )
        }
    }

    private suspend fun searchUsers(query: String) {
        dispatch(PeopleStore.Message.SetLoading)
        dispatch(PeopleStore.Message.SetLastSearchedQuery(query))

        when (val response = searchUsersUseCase(query)) {
            is ResultWrapper.NetworkError -> {
                publish(PeopleStore.Label.Toast(parseErrorMessage(response)))
            }
            is ResultWrapper.Failed -> {
                dispatch(PeopleStore.Message.SetError(response))
            }

            is ResultWrapper.Success -> {
                dispatch(PeopleStore.Message.SetUsers(response.data
                    .map { it.toUi() }
                    .toPersistentList()))
            }
        }
    }


    private suspend fun loadAllUsers() {
        dispatch(PeopleStore.Message.SetLoading)
        getAllUsersUseCase().collect { response ->
            when (response) {
                is ResultWrapper.NetworkError -> {
                    publish(PeopleStore.Label.Toast(parseErrorMessage(response)))
                }
                is ResultWrapper.Failed -> {
                    dispatch(PeopleStore.Message.SetError(response))
                }

                is ResultWrapper.Success -> {
                    dispatch(PeopleStore.Message.SetUsers(response.data
                        .map { it.toUi() }
                        .toPersistentList()))
                }
            }
        }
    }

    override fun onException(exception: Throwable) {
        super.onException(exception)
        dispatch(
            PeopleStore.Message.SetError(
                ResultWrapper.Failed(
                    exception = exception,
                    errorMessage = exception.message,
                )
            )
        )
    }

    override fun dispose() {
        super.dispose()
        loadUsersJob?.cancel()
    }
}