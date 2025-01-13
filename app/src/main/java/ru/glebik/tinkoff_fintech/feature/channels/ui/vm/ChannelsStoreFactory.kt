package ru.glebik.tinkoff_fintech.feature.channels.ui.vm

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.channels.di.StreamUiMapperQualifier
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.CreateStreamUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetStreamTopicsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import javax.inject.Inject

@FeatureScope
class ChannelsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @StreamUiMapperQualifier
    private val streamUiMapper: UiMapper<Stream, StreamUiModel>,

    private val getAllStreamsUseCase: GetAllStreamsUseCase,
    private val getAllSubscribedUseCase: GetSubscribedStreamsUseCase,
    private val searchSubscribedStreamsUseCase: SearchSubscribedStreamsUseCase,
    private val searchAllStreamsUseCase: SearchAllStreamsUseCase,
    private val getStreamTopicsUseCase: GetStreamTopicsUseCase,
    private val createStreamUseCase: CreateStreamUseCase,
) {
    fun create(): ChannelsStore = object :
        ChannelsStore,
        Store<ChannelsStore.Intent, ChannelsStore.State, ChannelsStore.Label> by storeFactory.create(
            name = ChannelsStore::class.simpleName,
            initialState = ChannelsStore.State(),
            bootstrapper = null,
            executorFactory = {
                ChannelsExecutor(
                    streamUiMapper,
                    getAllStreamsUseCase,
                    getAllSubscribedUseCase,
                    searchSubscribedStreamsUseCase,
                    searchAllStreamsUseCase,
                    getStreamTopicsUseCase,
                    createStreamUseCase
                )
            },
            reducer = ChannelsReducer(),
        ) {}
}
