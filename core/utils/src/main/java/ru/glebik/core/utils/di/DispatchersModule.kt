package ru.glebik.core.utils.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object DispatchersModule {

    @IoDispatcherQualifier
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcherQualifier
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcherQualifier
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}
