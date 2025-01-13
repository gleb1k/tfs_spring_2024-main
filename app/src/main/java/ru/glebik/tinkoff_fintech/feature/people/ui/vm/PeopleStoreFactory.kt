package ru.glebik.tinkoff_fintech.feature.people.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase
import javax.inject.Inject

@FeatureScope
class PeopleStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,

    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
) {
    fun create(): PeopleStore = object :
        PeopleStore,
        Store<PeopleStore.Intent, PeopleStore.State,  PeopleStore.Label> by storeFactory.create(
            name = PeopleStore::class.simpleName,
            initialState = PeopleStore.State(),
            bootstrapper = null,
            executorFactory = {
                PeopleExecutor(
                    getAllUsersUseCase,
                    searchUsersUseCase
                )
            },
            reducer = PeopleReducer(),
        ) {}

}
