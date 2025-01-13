package ru.glebik.tinkoff_fintech.feature.chat.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.wrapError
import ru.glebik.tinkoff_fintech.feature.chat.data.MessagesApi.Companion.BASE_MESSAGE_FETCH_COUNT
import ru.glebik.tinkoff_fintech.feature.chat.data.model.Narrow
import ru.glebik.tinkoff_fintech.feature.chat.data.model.NarrowItem
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.MessagesResponse
import javax.inject.Inject

@FeatureScope
class ChatLongPollService @Inject constructor(
    private val messagesApi: MessagesApi,
    private val refreshIntervalMs: Long = BASE_MESSAGE_REFRESH_INTERVAL_MS,
) {
    fun fetchNewestMessages(
        streamName: String,
        topicName: String?,
    ): Flow<ResultWrapper<MessagesResponse>> = flow {
        val narrow = Narrow()
        narrow.add(NarrowItem(Narrow.STREAM, streamName))
        if (topicName != null) {
            narrow.add(NarrowItem(Narrow.TOPIC, topicName))
        }
        while (true) {
            runCatching {
                messagesApi.getMessages(
                    anchor = ANCHOR_NEWEST,
                    numBefore = BASE_MESSAGE_FETCH_COUNT,
                    numAfter = 0,
                    narrow = narrow.toString(),
                )
            }.fold(
                onSuccess = { emit(ResultWrapper.Success(it)) },
                onFailure = { emit(wrapError(it)) }
            )
            delay(refreshIntervalMs)
        }
    }

    companion object {
        const val BASE_MESSAGE_REFRESH_INTERVAL_MS = 5000L
        const val ANCHOR_NEWEST = "newest"
    }
}
