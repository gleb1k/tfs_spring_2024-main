package ru.glebik.tinkoff_fintech.feature.user.data

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User

interface UserRepository {

    suspend fun getAllUsers(): Flow<ResultWrapper<List<User>>>

    suspend fun getOwnUser(): Flow<ResultWrapper<User>>

    suspend fun searchUsers(name: String): ResultWrapper<List<User>>

}