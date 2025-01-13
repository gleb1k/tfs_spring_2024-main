package ru.glebik.core.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class UsableAppNavigator(
    activity: FragmentActivity,
    containerId: Int
) : AppNavigator(activity, containerId) {
    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        if (screen !is UsableFragmentScreen) return

        val enter = screen.clientScreen.animEnterResId
        val exit = screen.clientScreen.animOutResId

        if  (enter != null && exit!= null) {
            fragmentTransaction.setCustomAnimations(enter, exit, enter, exit)
        }

    }
}