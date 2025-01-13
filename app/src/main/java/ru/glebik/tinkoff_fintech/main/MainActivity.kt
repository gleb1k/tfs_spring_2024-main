package ru.glebik.tinkoff_fintech.main

import android.os.Bundle
import ru.glebik.core.navigation.toCiceroneScreen
import ru.glebik.core.presentation.BaseActivity
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.main.di.appComponent


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            router.replaceScreen(BottomNavHostScreen.toCiceroneScreen())
    }
}