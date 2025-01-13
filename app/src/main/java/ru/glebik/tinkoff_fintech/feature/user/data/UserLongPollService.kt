package ru.glebik.tinkoff_fintech.feature.user.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.wrapError
import ru.glebik.tinkoff_fintech.feature.user.data.mapper.mapToUser
import ru.glebik.tinkoff_fintech.feature.user.data.model.UsersResponse
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import javax.inject.Inject

class UserLongPollService @Inject constructor(
    private val userApi: UserApi,
    private val refreshIntervalMs: Long = BASE_USER_REFRESH_INTERVAL,
) {
    fun fetchAllUsers(): Flow<ResultWrapper<UsersResponse>> = flow {
        while (true) {
            runCatching {
                userApi.users()
            }.fold(
                onSuccess = { emit(ResultWrapper.Success(it)) },
                onFailure = {
                    Log.e("User All LongPoll", it.toString())
                    emit(wrapError(it))
                }
            )
            delay(refreshIntervalMs)
        }
    }

    fun fetchOwnUser(): Flow<ResultWrapper<User>> = flow {
        while (true) {
            runCatching {
                val ownUser = userApi.ownUser()
                val userPresence = userApi.userPresence(ownUser.userId)
                val result = ownUser.mapToUser(userPresence.presence, null)

                result
            }.fold(
                onSuccess = { emit(ResultWrapper.Success(it)) },
                onFailure = {
                    Log.e("User Own LongPoll", it.toString())
                    emit(wrapError(it))
                }
            )
            delay(refreshIntervalMs)
        }
    }

    companion object {
        const val BASE_USER_REFRESH_INTERVAL = 30000L
    }
}
