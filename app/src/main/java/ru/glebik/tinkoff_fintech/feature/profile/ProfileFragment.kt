package ru.glebik.tinkoff_fintech.feature.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.glebik.core.navigation.ClientScreen
import ru.glebik.core.presentation.BaseComposeFragment
import ru.glebik.core.widget.composable.ErrorScreen
import ru.glebik.tinkoff_fintech.feature.profile.di.DaggerProfileComponent
import ru.glebik.tinkoff_fintech.feature.profile.ui.ProfileLoading
import ru.glebik.tinkoff_fintech.feature.profile.ui.ProfileScreen
import ru.glebik.tinkoff_fintech.feature.profile.ui.vm.ProfileStore
import ru.glebik.tinkoff_fintech.feature.profile.ui.vm.ProfileViewModel
import ru.glebik.tinkoff_fintech.main.di.appComponent
import javax.inject.Inject

object ProfileScreen : ClientScreen()

class ProfileFragment : BaseComposeFragment() {

    @Inject
    lateinit var store: ProfileStore

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.provideFactory(store)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.label.collect { label ->
                when (label) {
                    is ProfileStore.Label.Toast -> Toast.makeText(
                        context,
                        label.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @Composable
    override fun FragmentContent() {

        val state by viewModel.state.collectAsStateWithLifecycle()

        if (state.isLoading)
            ProfileLoading()
        else if (state.errorMessage != null)
            ErrorScreen(
                state.errorMessage.orEmpty(),
                viewModel::loadCurrentUser
            )
        else
            ProfileScreen(state = state)
    }

    override fun initDagger() {
        DaggerProfileComponent.factory().create(this.appComponent()).inject(this)
    }
}


