package ru.glebik.tinkoff_fintech.feature.profile.ui.vm

import ru.glebik.core.presentation.mvi.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.parseErrorMessage
import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCase
import ru.glebik.tinkoff_fintech.feature.user.ui.model.mapper.toUi

class ProfileExecutor(
    private val getOwnUserUseCase: GetOwnUserUseCase,
) : BaseExecutor<ProfileStore.Intent, Unit, ProfileStore.State, ProfileStore.Message,  ProfileStore.Label>() {

    override fun executeIntent(
        intent: ProfileStore.Intent,
        getState: () -> ProfileStore.State,
    ) {
        when (intent) {
            ProfileStore.Intent.LoadProfileData -> scope.launchSafe {
                loadProfile()
            }
        }
    }

    private suspend fun loadProfile() {
        dispatch(ProfileStore.Message.SetLoading)
        getOwnUserUseCase().collect { response ->
            when (response) {
                is ResultWrapper.NetworkError -> {
                    publish(ProfileStore.Label.Toast(parseErrorMessage(response)))
                }
                is ResultWrapper.Failed -> {
                    dispatch(ProfileStore.Message.SetError(response))
                }

                is ResultWrapper.Success -> {
                    dispatch(ProfileStore.Message.SetProfile(response.data.toUi()))
                }
            }
        }
    }

    override fun onException(exception: Throwable) {
        super.onException(exception)
        dispatch(
            ProfileStore.Message.SetError(
                ResultWrapper.Failed(
                    exception = exception,
                    errorMessage = exception.message,
                )
            )
        )
    }
}