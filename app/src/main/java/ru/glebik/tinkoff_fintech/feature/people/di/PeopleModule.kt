package ru.glebik.tinkoff_fintech.feature.people.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import ru.glebik.tinkoff_fintech.feature.people.ui.vm.PeopleStore
import ru.glebik.tinkoff_fintech.feature.people.ui.vm.PeopleStoreFactory
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase

@Module
interface PeopleModule {
    companion object {
        @Provides
        fun providePeopleStore(
            storeFactory: StoreFactory,
            getAllUsersUseCase: GetAllUsersUseCase,
            searchUsersUseCase: SearchUsersUseCase,
        ): PeopleStore = PeopleStoreFactory(
            storeFactory, getAllUsersUseCase, searchUsersUseCase
        ).create()
    }
}