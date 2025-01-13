package ru.glebik.tinkoff_fintech.feature.channels.ui.vm

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import ru.glebik.core.presentation.mvi.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.core.utils.parseErrorMessage
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.CreateStreamUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetStreamTopicsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper.toUi

class ChannelsExecutor(
    private val streamUiMapper: UiMapper<Stream, StreamUiModel>,

    private val getAllStreamsUseCase: GetAllStreamsUseCase,
    private val getSubscribedStreamsUseCase: GetSubscribedStreamsUseCase,
    private val searchSubscribedStreamsUseCase: SearchSubscribedStreamsUseCase,
    private val searchAllStreamsUseCase: SearchAllStreamsUseCase,
    private val getStreamTopicsUseCase: GetStreamTopicsUseCase,
    private val createStreamUseCase: CreateStreamUseCase,
) : BaseExecutor<ChannelsStore.Intent, Unit, ChannelsStore.State, ChannelsStore.Message, ChannelsStore.Label>() {

    private var loadAllStreamsJob: Job? = null
    private var loadSubscribedJob: Job? = null
    private var searchJob: Job? = null

    override fun executeIntent(intent: ChannelsStore.Intent, getState: () -> ChannelsStore.State) {
        when (intent) {
            ChannelsStore.Intent.LoadAllStreams -> {
                if (loadAllStreamsJob?.isActive == true) return

                loadAllStreamsJob = scope.launchSafe {
                    loadAllStreams()
                }
            }

            ChannelsStore.Intent.LoadSubscribedStreams -> {
                if (loadSubscribedJob?.isActive == true) return

                loadSubscribedJob = scope.launchSafe {
                    loadSubscribedStreams()
                }
            }

            is ChannelsStore.Intent.OnSearchQueryChange -> dispatch(
                ChannelsStore.Message.SetSearchQuery(intent.query)
            )

            is ChannelsStore.Intent.OnSearchVisibleChange -> dispatch(
                ChannelsStore.Message.SetSearchVisible(intent.isVisible)
            )

            is ChannelsStore.Intent.OnCollapseModeChange -> scope.launchSafe {
                changeCollapseMode(
                    intent.stream,
                    getState()
                )
            }

            is ChannelsStore.Intent.SearchAllStreams -> {
                if (searchJob?.isActive == true) return

                searchJob = scope.launchSafe {
                    searchAllStreams(intent.query)
                }
            }

            is ChannelsStore.Intent.SearchSubscribedStreams -> {
                if (searchJob?.isActive == true) return

                searchJob = scope.launchSafe {
                    searchSubscribedStreams(intent.query)
                }
            }

            is ChannelsStore.Intent.ChangeTabPage -> changeTabPage(intent.pageIndex)
            is ChannelsStore.Intent.LoadStreamTopics -> scope.launchSafe {
                loadTopics(intent.stream, getState())
            }

            is ChannelsStore.Intent.CreateStream -> scope.launchSafe {
                createStream(intent.name)
            }

            is ChannelsStore.Intent.OnAddChannelDialogQueryChange -> dispatch(
                ChannelsStore.Message.SetAddChannelDialogQuery(intent.query)
            )

            is ChannelsStore.Intent.OnShowAddChannelDialog -> dispatch(
                ChannelsStore.Message.SetAddChannelDialogVisibility(intent.isShow)
            )
        }
    }

    private suspend fun createStream(name: String) {
        when (val response = createStreamUseCase(name)) {
            is ResultWrapper.NetworkError -> {
                publish(ChannelsStore.Label.Toast(parseErrorMessage(response)))
            }
            is ResultWrapper.Failed -> dispatch(ChannelsStore.Message.SetError(response))
            is ResultWrapper.Success -> {
                loadSubscribedStreams()
                loadAllStreams()
            }
        }
    }

    private suspend fun loadTopics(stream: StreamUiModel, state: ChannelsStore.State) {
        getStreamTopicsUseCase(stream.id).collect { result ->
            when (result) {
                is ResultWrapper.NetworkError -> {
                    publish(ChannelsStore.Label.Toast(parseErrorMessage(result)))
                }
                is ResultWrapper.Failed -> dispatch(ChannelsStore.Message.SetError(result))
                is ResultWrapper.Success -> {
                    val currentList =
                        if (state.selectedPage == ChannelsStore.ChannelTabPage.SUBSCRIBED) {
                            state.subscribedStreams
                        } else {
                            state.allStreams
                        }

                    val updatedList = currentList.map {
                        if (it.id == stream.id) {

                            StreamUiModel(
                                name = stream.name,
                                topics = result.data
                                    .map { topic: Topic -> topic.toUi() }
                                    .toPersistentList(),
                                isCollapsed = it.isCollapsed,
                                id = stream.id,
                                isTopicsLoading = false
                            )
                        } else {
                            it
                        }
                    }.toPersistentList()

                    if (state.selectedPage == ChannelsStore.ChannelTabPage.SUBSCRIBED)
                        dispatch(ChannelsStore.Message.SetSubscribedStreams(updatedList))
                    else {
                        dispatch(ChannelsStore.Message.SetAllStreams(updatedList))
                    }
                }
            }
        }
    }

    private fun changeCollapseMode(stream: StreamUiModel, state: ChannelsStore.State) {
        val currentList =
            if (state.selectedPage == ChannelsStore.ChannelTabPage.SUBSCRIBED) {
                state.subscribedStreams
            } else {
                state.allStreams
            }

        val updatedList = currentList.map {
            if (it.id == stream.id) {
                StreamUiModel(
                    name = stream.name,
                    topics = stream.topics,
                    isCollapsed = !stream.isCollapsed,
                    id = stream.id,
                    isTopicsLoading = stream.topics.isEmpty()
                )
            } else {
                it
            }
        }.toPersistentList()

        if (state.selectedPage == ChannelsStore.ChannelTabPage.SUBSCRIBED)
            dispatch(ChannelsStore.Message.SetSubscribedStreams(updatedList))
        else {
            dispatch(ChannelsStore.Message.SetAllStreams(updatedList))
        }
    }

    private fun changeTabPage(pageIndex: Int) {
        dispatch(ChannelsStore.Message.SelectTabPage(pageIndex))
    }

    private suspend fun searchAllStreams(query: String) {
        dispatch(ChannelsStore.Message.SetLoading)
        dispatch(ChannelsStore.Message.SetLastSearchedQuery(query))

        when (val response = searchAllStreamsUseCase(query)) {
            is ResultWrapper.NetworkError -> {
                publish(ChannelsStore.Label.Toast(parseErrorMessage(response)))
            }
            is ResultWrapper.Failed -> {
                dispatch(ChannelsStore.Message.SetError(response))
            }

            is ResultWrapper.Success -> {
                dispatch(ChannelsStore.Message.SetAllStreams(response.data.map {
                    streamUiMapper.toUi(
                        it
                    )
                }.toPersistentList()))
            }
        }
    }

    private suspend fun searchSubscribedStreams(query: String) {
        dispatch(ChannelsStore.Message.SetLoading)
        dispatch(ChannelsStore.Message.SetLastSearchedQuery(query))

        when (val response = searchSubscribedStreamsUseCase(query)) {
            is ResultWrapper.NetworkError -> {
                publish(ChannelsStore.Label.Toast(parseErrorMessage(response)))
            }
            is ResultWrapper.Failed -> {
                dispatch(ChannelsStore.Message.SetError(response))
            }

            is ResultWrapper.Success -> {
                dispatch(ChannelsStore.Message.SetSubscribedStreams(response.data.map {
                    streamUiMapper.toUi(
                        it
                    )
                }.toPersistentList()))
            }
        }
    }

    private suspend fun loadAllStreams() {
        dispatch(ChannelsStore.Message.SetLoading)

        getAllStreamsUseCase().collect { streams ->
            when (streams) {
                is ResultWrapper.NetworkError -> {
                    publish(ChannelsStore.Label.Toast(parseErrorMessage(streams)))
                }
                is ResultWrapper.Failed -> {
                    dispatch(ChannelsStore.Message.SetError(streams))
                }

                is ResultWrapper.Success -> {
                    if (streams.data.isNotEmpty()) {
                        dispatch(ChannelsStore.Message.SetAllStreams(streams.data.map {
                            streamUiMapper.toUi(
                                it
                            )
                        }.toPersistentList()))
                    }
                }

            }
        }
    }

    private suspend fun loadSubscribedStreams() {
        dispatch(ChannelsStore.Message.SetLoading)

        getSubscribedStreamsUseCase().collect { streams ->
            when (streams) {
                is ResultWrapper.NetworkError -> {
                    publish(ChannelsStore.Label.Toast(parseErrorMessage(streams)))
                }
                is ResultWrapper.Failed -> {
                    dispatch(ChannelsStore.Message.SetError(streams))
                }

                is ResultWrapper.Success -> {
                    if (streams.data.isNotEmpty()) {
                        dispatch(
                            ChannelsStore.Message.SetSubscribedStreams(streams.data.map {
                                streamUiMapper.toUi(
                                    it
                                )
                            }.toPersistentList())
                        )
                    }
                }
            }
        }
    }

    override fun onException(exception: Throwable) {
        super.onException(exception)
        dispatch(
            ChannelsStore.Message.SetError(
                ResultWrapper.Failed(
                    exception = exception,
                    errorMessage = exception.message,
                )
            )
        )
    }

    override fun dispose() {
        super.dispose()
        loadSubscribedJob?.cancel()
        loadAllStreamsJob?.cancel()
        searchJob?.cancel()
    }
}