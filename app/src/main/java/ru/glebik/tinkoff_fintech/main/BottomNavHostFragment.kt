package ru.glebik.tinkoff_fintech.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.glebik.core.navigation.ClientScreen
import ru.glebik.core.presentation.BaseFragment
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.databinding.FragmentBnvHostBinding
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment
import ru.glebik.tinkoff_fintech.feature.people.PeopleFragment
import ru.glebik.tinkoff_fintech.feature.profile.ProfileFragment

object BottomNavHostScreen : ClientScreen()

class BottomNavHostFragment : BaseFragment() {

    private lateinit var bnvMain: BottomNavigationView

    override fun initDagger() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bnv_host, container, false).also {
            bnvMain = FragmentBnvHostBinding.bind(it).bnvMain

            bnvMain.setOnItemSelectedListener { item ->

                when (item.itemId) {
                    R.id.action_channels -> {
                        handleMovement(ChannelsFragment())
                        true
                    }

                    R.id.action_people -> {
                        handleMovement(PeopleFragment())
                        true
                    }

                    R.id.action_profile -> {
                        handleMovement(ProfileFragment())
                        true
                    }

                    else -> false
                }
            }
            if (savedInstanceState == null)
                bnvMain.selectedItemId = R.id.action_channels
        }
    }

    private fun handleMovement(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.bnv_content_container, fragment)
        transaction.commit()
    }
}