package ru.glebik.tinkoff_fintech.feature.people.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase
import ru.glebik.tinkoff_fintech.main.di.BaseUiFeatureDeps

interface PeopleDeps : BaseUiFeatureDeps {

    @DefaultDispatcherQualifier
    fun defaultDispatcher(): CoroutineDispatcher

    fun getAllUsersUseCase(): GetAllUsersUseCase

    fun searchUsersUseCase(): SearchUsersUseCase
}