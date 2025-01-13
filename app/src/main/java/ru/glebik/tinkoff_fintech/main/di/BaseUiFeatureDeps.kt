package ru.glebik.tinkoff_fintech.main.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.terrakok.cicerone.Router

interface BaseUiFeatureDeps {
    fun router(): Router

    fun storeFactory(): StoreFactory
}