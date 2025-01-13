package ru.glebik.tinkoff_fintech.feature.channels.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepository
import ru.glebik.tinkoff_fintech.feature.channels.data.ChannelsRepositoryImpl
import ru.glebik.tinkoff_fintech.feature.channels.data.StreamsApi
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.CreateStreamUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.CreateStreamUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetAllStreamsUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetStreamTopicsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetStreamTopicsUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.GetSubscribedStreamsUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchAllStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchAllStreamsUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchSubscribedStreamsUseCase
import ru.glebik.tinkoff_fintech.feature.channels.domain.usecase.SearchSubscribedStreamsUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TopicUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper.StreamUiMapper
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper.TopicUiMapper
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsStore
import ru.glebik.tinkoff_fintech.feature.channels.ui.vm.ChannelsStoreFactory


@Module
interface ChannelsModule {

    @Binds
    fun bindRepository(channelsRepositoryImpl: ChannelsRepositoryImpl): ChannelsRepository

    @TopicUiMapperQualifier
    @Binds
    fun bindTopicUiMapper(topicUiMapper: TopicUiMapper): UiMapper<Topic, TopicUiModel>

    @StreamUiMapperQualifier
    @Binds
    fun bindStreamUiMapper(streamUiMapper: StreamUiMapper): UiMapper<Stream, StreamUiModel>

    @Binds
    fun bindGetAllStreamsUseCase(getAllStreamsUseCaseImpl: GetAllStreamsUseCaseImpl): GetAllStreamsUseCase

    @Binds
    fun bindGetAllSubscribedUseCase(getSubscribedStreamsUseCaseImpl: GetSubscribedStreamsUseCaseImpl): GetSubscribedStreamsUseCase

    @Binds
    fun bindSearchAllStreamsUseCase(searchAllStreamsUseCaseImpl: SearchAllStreamsUseCaseImpl): SearchAllStreamsUseCase

    @Binds
    fun bindSearchSubscribedStreamsUseCase(searchSubscribedStreamsUseCaseImpl: SearchSubscribedStreamsUseCaseImpl): SearchSubscribedStreamsUseCase

    @Binds
    fun bindCreateStreamUseCase(createStreamUseCaseImpl: CreateStreamUseCaseImpl): CreateStreamUseCase

    @Binds
    fun bindGetStreamTopicsUseCase(getStreamTopicsUseCaseImpl: GetStreamTopicsUseCaseImpl): GetStreamTopicsUseCase

    companion object {

        @Provides
        fun provideChannelsStore(
            channelsStoreFactory: ChannelsStoreFactory,
        ): ChannelsStore = channelsStoreFactory.create()

        @Provides
        fun provideChannelsApi(
            retrofit: Retrofit,
        ): StreamsApi = retrofit.create(StreamsApi::class.java)
    }

}