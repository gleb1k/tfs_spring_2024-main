package ru.glebik.tinkoff_fintech.feature.profile.ui.vm

import com.arkivanov.mvikotlin.core.store.Reducer

class ProfileReducer : Reducer<ProfileStore.State, ProfileStore.Message> {

    override fun ProfileStore.State.reduce(
        msg: ProfileStore.Message,
    ) = when (msg) {
        is ProfileStore.Message.SetError -> copy(
            errorMessage = msg.error.errorMessage,
            isLoading = false,
        )

        ProfileStore.Message.SetLoading -> copy(
            isLoading = true,
            errorMessage = null,
        )

        is ProfileStore.Message.SetProfile -> copy(
            user = msg.user,
            errorMessage = null,
            isLoading = false,
        )
    }
}