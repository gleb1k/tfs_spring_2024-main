package ru.glebik.tinkoff_fintech.feature.chat.data

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.glebik.core.network.response.BaseInfoResponse
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.FetchSingleMessageResponse
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.MessagesResponse
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.SendMessageResponse

interface MessagesApi {

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String,

        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("client_gravatar") clientGravatar: Boolean = true,
    ): MessagesResponse

    @GET("messages/{message_id}")
    suspend fun getMessageById(
        @Path("message_id") messageId: Int,

        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): FetchSingleMessageResponse

    @POST("messages")
    suspend fun sendMessage(
        //stream stream
        @Query("type") type: String,
        //stream name
        @Query("to") to: String,
        //topic name
        @Query("topic") topic: String?,
        @Query("content") content: String,
    ): SendMessageResponse

    @DELETE("messages/{message_id}")
    suspend fun deleteMessage(
        @Path("message_id") messageId: Int,
    ) : BaseInfoResponse

    @PATCH("messages/{message_id}")
    suspend fun editMessageContent(
        @Path("message_id") messageId: Int,
        @Query("content") content: String,
    ): BaseInfoResponse

    @PATCH("messages/{message_id}")
    suspend fun editMessageTopic(
        @Path("message_id") messageId: Int,
        @Query("topic") topic: String?,
    ): BaseInfoResponse

    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): BaseInfoResponse

    @DELETE("messages/{message_id}/reactions")
    suspend fun removeReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): BaseInfoResponse

    companion object {
        const val BASE_MESSAGE_FETCH_COUNT = 20
    }
}