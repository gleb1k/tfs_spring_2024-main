package ru.glebik.tinkoff_fintech.feature.profile.ui.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.glebik.core.presentation.mvi.MviViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val store: ProfileStore,
) : MviViewModel<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label>(
    store
) {

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() = store.accept(ProfileStore.Intent.LoadProfileData)

    companion object {

        fun provideFactory(
            store: ProfileStore,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProfileViewModel(
                    store
                )
            }
        }
    }
}