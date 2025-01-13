package ru.glebik.tinkoff_fintech.feature.channels.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.CreateStreamResponse
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.StreamsResponse
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.TopicsResponse
import ru.glebik.tinkoff_fintech.feature.channels.data.model.response.UnreadMessagesResponse

interface StreamsApi {

    @GET("users/me/subscriptions")
    suspend fun subscriptions(): StreamsResponse

    @GET("streams")
    suspend fun allStreams(): StreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun streamTopics(
        @Path("stream_id") streamId: Int,
    ): TopicsResponse

    @POST("register")
    suspend fun getAllTopics(
        @Query("event_types") eventTypes: String? = "[\"message\", \"update_message_flags\"]",
        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): UnreadMessagesResponse

    @POST("users/me/subscriptions")
    suspend fun createStream(
        @Query("subscriptions") subscriptionsParam: String,
    ): CreateStreamResponse

}