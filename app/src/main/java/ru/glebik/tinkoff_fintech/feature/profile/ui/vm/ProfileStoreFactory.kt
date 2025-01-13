package ru.glebik.tinkoff_fintech.feature.profile.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCase
import javax.inject.Inject

@FeatureScope
class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,

    private val getOwnUserUseCase: GetOwnUserUseCase,
) {
    fun create(): ProfileStore = object :
        ProfileStore,
        Store<ProfileStore.Intent, ProfileStore.State,  ProfileStore.Label> by storeFactory.create(
            name = ProfileStore::class.simpleName,
            initialState = ProfileStore.State(),
            bootstrapper = null,
            executorFactory = {
                ProfileExecutor(
                    getOwnUserUseCase
                )
            },
            reducer = ProfileReducer(),
        ) {}

}
