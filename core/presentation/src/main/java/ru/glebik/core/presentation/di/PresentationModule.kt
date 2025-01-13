package ru.glebik.core.presentation.di

import android.util.Log
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
object PresentationModule {

    @Provides
    fun provideLogger() : Logger =  object : Logger {
        override fun log(text: String) {
            Log.v("StoreFactoryLogger", text)
        }
    }

    @Provides
    fun provideLoggingStoreFactory(
        logger : Logger
    ) : StoreFactory = LoggingStoreFactory(DefaultStoreFactory(), logger = logger)

}
