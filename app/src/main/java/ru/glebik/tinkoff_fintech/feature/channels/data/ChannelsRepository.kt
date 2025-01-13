package ru.glebik.tinkoff_fintech.feature.channels.data

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.channels.data.model.SubscriptionsParams
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic

interface ChannelsRepository {

    suspend fun getSubscribed(): Flow<ResultWrapper<List<Stream>>>

    suspend fun getAllStreams(): Flow<ResultWrapper<List<Stream>>>

    suspend fun searchSubscribed(name: String): ResultWrapper<List<Stream>>

    suspend fun searchAllStreams(name: String): ResultWrapper<List<Stream>>

    suspend fun getStreamTopics(streamId: Int): Flow<ResultWrapper<List<Topic>>>

    suspend fun createStream(subscriptionsParams: SubscriptionsParams): ResultWrapper<String>

}