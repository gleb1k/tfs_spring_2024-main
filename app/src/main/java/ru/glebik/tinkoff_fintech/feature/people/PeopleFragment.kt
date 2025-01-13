package ru.glebik.tinkoff_fintech.feature.people

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.navigation.ClientScreen
import ru.glebik.core.presentation.BaseComposeFragment
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.widget.composable.ErrorScreen
import ru.glebik.core.widget.composable.SearchTopBar
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.people.di.DaggerPeopleComponent
import ru.glebik.tinkoff_fintech.feature.people.ui.LoadingPeopleList
import ru.glebik.tinkoff_fintech.feature.people.ui.UserItem
import ru.glebik.tinkoff_fintech.feature.people.ui.vm.PeopleStore
import ru.glebik.tinkoff_fintech.feature.people.ui.vm.PeopleViewModel
import ru.glebik.tinkoff_fintech.main.di.appComponent
import javax.inject.Inject

object PeopleScreen : ClientScreen()

class PeopleFragment : BaseComposeFragment() {

    @Inject
    lateinit var store: PeopleStore

    @Inject
    @DefaultDispatcherQualifier
    lateinit var defaultDispatcher: CoroutineDispatcher

    private val viewModel: PeopleViewModel by viewModels {
        PeopleViewModel.provideFactory(store, defaultDispatcher)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.label.collect { label ->
                when (label) {
                    is PeopleStore.Label.Toast -> Toast.makeText(
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

        Scaffold(
            modifier = Modifier
                .fillMaxHeight(),
            containerColor = FintechTheme.colors.background,
            topBar = {
                SearchTopBar(
                    stringResource(id = R.string.people_title),
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    isSearchEnabled = state.isSearchVisible,
                    onSearchVisibleChange = viewModel::onSearchVisibleChange,
                    onSearch = viewModel::searchUsers
                )
            }
        ) {
            if (state.isLoading)
                LoadingPeopleList(it)
            else if (state.errorMessage != null)
                ErrorScreen(
                    state.errorMessage.orEmpty(),
                    viewModel::loadAllUsers
                )
            else
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        items = state.users,
                        key = { _, item -> item.id }) { _, user ->
                        UserItem(user)
                    }
                }
        }
    }

    override fun initDagger() {
        DaggerPeopleComponent.factory().create(this.appComponent()).inject(this)
    }

    companion object {
        const val SHIMMER_LOADING_COUNT = 5
    }

}