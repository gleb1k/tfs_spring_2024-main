package ru.glebik.tinkoff_fintech.main

import android.app.Application
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.glebik.core.navigation.SCREEN_MAP
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsScreen
import ru.glebik.tinkoff_fintech.feature.chat.CHAT_SCREEN
import ru.glebik.tinkoff_fintech.feature.people.PeopleFragment
import ru.glebik.tinkoff_fintech.feature.people.PeopleScreen
import ru.glebik.tinkoff_fintech.feature.profile.ProfileFragment
import ru.glebik.tinkoff_fintech.feature.profile.ProfileScreen
import ru.glebik.tinkoff_fintech.main.di.AppComponent
import ru.glebik.tinkoff_fintech.main.di.AppComponentProvider
import ru.glebik.tinkoff_fintech.main.di.DaggerAppComponent

open class App : Application(), AppComponentProvider {

    override lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)

        initializeNavigation()
    }

    private fun initializeNavigation() {
        SCREEN_MAP.apply {
            putAll(CHAT_SCREEN)
            put(ChannelsScreen::class.java) { FragmentScreen { ChannelsFragment() } }
            put(ProfileScreen::class.java) { FragmentScreen { ProfileFragment() } }
            put(PeopleScreen::class.java) { FragmentScreen { PeopleFragment() } }
            put(BottomNavHostScreen::class.java) { FragmentScreen { BottomNavHostFragment() } }
        }
    }

}