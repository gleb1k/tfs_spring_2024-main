package ru.glebik.tinkoff_fintech.feature.channels.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.glebik.core.db.api.dao.StreamDao
import ru.glebik.core.db.api.dao.TopicDao
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.core.utils.wrapError
import ru.glebik.tinkoff_fintech.feature.channels.data.model.SubscriptionsParams
import ru.glebik.tinkoff_fintech.feature.channels.data.model.mapper.toDomain
import ru.glebik.tinkoff_fintech.feature.channels.data.model.mapper.toEntity
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import java.util.Locale
import javax.inject.Inject

@FeatureScope
class ChannelsRepositoryImpl @Inject constructor(
    @DefaultDispatcherQualifier
    private val defaultDispatcher: CoroutineDispatcher,

    @IoDispatcherQualifier
    private val ioDispatcher: CoroutineDispatcher,

    private val streamsApi: StreamsApi,

    private val topicDao: TopicDao,
    private val streamDao: StreamDao,
) : ChannelsRepository {

    override suspend fun getSubscribed(): Flow<ResultWrapper<List<Stream>>> = flow {
        runCatching {
            val cacheSubscribed =
                withContext(ioDispatcher) { streamDao.getSubscribed() }.map {
                    it.toDomain()
                }.sortedBy { it.name }
            cacheSubscribed
        }.fold(
            onSuccess = { emit(ResultWrapper.Success(it)) },
            onFailure = { emit(wrapError(it)) }
        )

        runCatching {
            val streamsResponse =
                withContext(ioDispatcher) { streamsApi.subscriptions().streamsResponse ?: listOf() }

            val streamsEntity = streamsResponse.map {
                it.toEntity(true)
            }

            streamDao.insertAll(streamsEntity)

            val streamsDomain = streamsResponse.map {
                it.toDomain()
            }.sortedBy { it.name }
            streamsDomain
        }.fold(
            onSuccess = { emit(ResultWrapper.Success(it)) },
            onFailure = {
                Log.e("getSubscribed", it.toString())
                emit(wrapError(it))
            }
        )
    }

    override suspend fun getAllStreams(): Flow<ResultWrapper<List<Stream>>> = flow {
        runCatching {
            val cacheSubscribed =
                withContext(ioDispatcher) { streamDao.getAll() }.map {
                    it.toDomain()
                }.sortedBy { it.name }
            cacheSubscribed
        }.fold(
            onSuccess = { emit(ResultWrapper.Success(it)) },
            onFailure = { emit(wrapError(it)) }
        )

        runCatching {
            val streamsResponse =
                withContext(ioDispatcher) { streamsApi.allStreams().streamsResponse ?: listOf() }

            val streamsAllEntity = streamsResponse.map {
                it.toEntity(false)
            }

            streamDao.insertAll(streamsAllEntity)

            val resultStreams = streamsResponse.map {
                it.toDomain()
            }.sortedBy { it.name }
            resultStreams
        }.fold(
            onSuccess = { emit(ResultWrapper.Success(it)) },
            onFailure = {
                Log.e("getAllStreams", it.toString())
                emit(wrapError(it))
            }
        )
    }

    override suspend fun searchSubscribed(name: String): ResultWrapper<List<Stream>> =
        runCatching {

            val subscribed = withContext(ioDispatcher) { streamDao.getSubscribed() }.map {
                it.toDomain()
            }

            subscribed.filter {
                it.name.lowercase(Locale.ROOT).contains(name.lowercase(Locale.ROOT))
            }.sortedBy { it.name }
        }.fold(
            onSuccess = { ResultWrapper.Success(it) },
            onFailure = { wrapError(it) }
        )


    override suspend fun searchAllStreams(name: String): ResultWrapper<List<Stream>> =
        runCatching {

            val all = withContext(ioDispatcher) { streamDao.getAll() }.map {
                it.toDomain()
            }

            all.filter {
                it.name.lowercase(Locale.ROOT).contains(name.lowercase(Locale.ROOT))
            }.sortedBy { it.name }
        }.fold(
            onSuccess = { ResultWrapper.Success(it) },
            onFailure = { wrapError(it) }
        )


    override suspend fun getStreamTopics(streamId: Int): Flow<ResultWrapper<List<Topic>>> =
        flow<ResultWrapper<List<Topic>>> {
            val cacheTopics =
                withContext(ioDispatcher) { topicDao.getAllTopicsByStreamId(streamId) }.map {
                    it.toDomain()
                }.sortedBy { it.name }

            if (cacheTopics.isNotEmpty()) {
                emit(ResultWrapper.Success(cacheTopics))
            }

            val topicsResponse = withContext(ioDispatcher) {
                streamsApi.streamTopics(streamId).topicResponses ?: listOf()
            }
            val unreadMsgResponse = withContext(ioDispatcher) { streamsApi.getAllTopics() }

            val topicsEntity = topicsResponse.map {
                val unreadCount = unreadMsgResponse.getUnreadCount(streamId, it.name.orEmpty())
                it.toEntity(streamId, unreadCount)
            }

            topicDao.replaceAll(streamId, topicsEntity)
            val topicsDomain = topicsResponse.map {
                val unreadCount = unreadMsgResponse.getUnreadCount(streamId, it.name.orEmpty())
                it.toDomain(unreadCount)
            }.sortedBy { it.name }
            emit(ResultWrapper.Success(topicsDomain))

        }.catch {
            emit(wrapError(it))
        }

    override suspend fun createStream(subscriptionsParams: SubscriptionsParams): ResultWrapper<String> =
        runCatching {
            withContext(ioDispatcher) { streamsApi.createStream(subscriptionsParams.toString()) }
        }.fold(
            onSuccess = { ResultWrapper.Success(it.result) },
            onFailure = { wrapError(it) },
        )
}
