package ru.glebik.core.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.glebik.core.navigation.ClientScreen

interface UsableFragmentScreen : FragmentScreen {

    val clientScreen: ClientScreen

    companion object {
        operator fun invoke(
            screen: ClientScreen,
            key: String? = null,
            clearContainer: Boolean = true,
            fragmentCreator: Creator<FragmentFactory, Fragment>
        ) = object : UsableFragmentScreen {
            override val clientScreen: ClientScreen = screen
            override val screenKey = key ?: fragmentCreator::class.java.name
            override val clearContainer = clearContainer
            override fun createFragment(factory: FragmentFactory) = fragmentCreator.create(factory)
        }
    }
}