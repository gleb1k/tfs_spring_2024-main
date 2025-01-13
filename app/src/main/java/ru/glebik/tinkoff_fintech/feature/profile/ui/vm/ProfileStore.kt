package ru.glebik.tinkoff_fintech.feature.profile.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import ru.glebik.core.presentation.mvi.ScreenState
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.ui.model.UserUiModel

interface ProfileStore : Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

    data class State internal constructor(
        val user: UserUiModel? = null,

        override val isLoading: Boolean = false,
        override val errorMessage: String? = null,
    ) : ScreenState

    sealed interface Intent {
        data object LoadProfileData : Intent
    }

    sealed interface Message {
        data class SetProfile(val user: UserUiModel) : Message
        data object SetLoading : Message
        data class SetError(val error: ResultWrapper.Failed) : Message
    }

    sealed interface Label {
        data class Toast(val message: String) : Label
    }

}