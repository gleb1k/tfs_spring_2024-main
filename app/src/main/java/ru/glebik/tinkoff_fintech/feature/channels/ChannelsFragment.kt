package ru.glebik.tinkoff_fintech.feature.channels

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import ru.glebik.tinkoff_fintech.feature.channels.di.DaggerChannelsComponent
import ru.glebik.tinkoff_fintech.feature.channels.ui.ChannelsTabs
import ru.glebik.tinkoff_fintech.feature.channels.ui.MainContent
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsStore
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsViewModel
import ru.glebik.tinkoff_fintech.main.di.appComponent
import javax.inject.Inject

object ChannelsScreen : ClientScreen()

@OptIn(ExperimentalFoundationApi::class)
class ChannelsFragment : BaseComposeFragment() {

    @Inject
    lateinit var store: ChannelsStore

    @Inject
    @DefaultDispatcherQualifier
    lateinit var defaultDispatcher: CoroutineDispatcher

    private val viewModel: ChannelsViewModel by viewModels {
        ChannelsViewModel.provideFactory(store, defaultDispatcher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.label.collect { label ->
                when (label) {
                    is ChannelsStore.Label.Toast -> Toast.makeText(
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
                    getString(R.string.channels_title),
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    isSearchEnabled = state.isSearchVisible,
                    onSearchVisibleChange = viewModel::onSearchVisibleChange,
                    onSearch = viewModel::searchStreams
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onAddChannelDialogVisibleChange(true) },
                    containerColor = FintechTheme.colors.primary,
                    contentColor = FintechTheme.colors.white,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        Icons.Filled.Add, stringResource(id = R.string.create_channel),
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {

                val pagerState = rememberPagerState(pageCount = state.tabs::size)

                ChannelsTabs(state, viewModel, pagerState)

                if (state.errorMessage != null)
                    ErrorScreen(
                        state.errorMessage.orEmpty(),
                        if (state.selectedPage == ChannelsStore.ChannelTabPage.SUBSCRIBED)
                            viewModel::loadSubscribed
                        else viewModel::loadAllStreams
                    )
                else
                    MainContent(state = state, viewModel, pagerState, router)

            }
        }
    }

    override fun initDagger() {
        DaggerChannelsComponent.factory().create(this.appComponent()).inject(this)
    }

    companion object {
        const val ANIMATION_DURATION = 500

        const val SHIMMER_STREAMS_ITEMS_COUNT = 5
        const val SHIMMER_TOPICS_ITEMS_COUNT = 3
    }
}