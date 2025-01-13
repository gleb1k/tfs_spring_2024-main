package ru.glebik.tinkoff_fintech.util

import ru.glebik.tinkoff_fintech.di.DaggerTestAppComponent
import ru.glebik.tinkoff_fintech.di.TestAppComponent
import ru.glebik.tinkoff_fintech.main.App

class TestApp : App() {

    lateinit var testAppComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()

        testAppComponent = DaggerTestAppComponent.factory().create(this)

    }

    fun getAppComponent(): TestAppComponent {
        return testAppComponent
    }
}
