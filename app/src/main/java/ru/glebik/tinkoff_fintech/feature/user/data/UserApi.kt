package ru.glebik.tinkoff_fintech.feature.user.data

import retrofit2.http.GET
import retrofit2.http.Path
import ru.glebik.tinkoff_fintech.feature.user.data.model.OwnUserResponse
import ru.glebik.tinkoff_fintech.feature.user.data.model.UsersResponse
import ru.glebik.tinkoff_fintech.feature.user.data.model.presence.MapUsersPresenceResponse
import ru.glebik.tinkoff_fintech.feature.user.data.model.presence.SingleUserPresenceResponse

interface UserApi {
    @GET("users")
    suspend fun users(): UsersResponse

    @GET("users/me")
    suspend fun ownUser(): OwnUserResponse

    @GET("users/{user_id_or_email}/presence")
    suspend fun userPresence(
        @Path("user_id_or_email") userId: Int,
    ): SingleUserPresenceResponse

    @GET("realm/presence")
    suspend fun getAllUserPresences(): MapUsersPresenceResponse

}