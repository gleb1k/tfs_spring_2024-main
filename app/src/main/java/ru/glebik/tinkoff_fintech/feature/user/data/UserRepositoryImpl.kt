package ru.glebik.tinkoff_fintech.feature.user.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import ru.glebik.core.db.api.dao.UserDao
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.core.utils.wrapError
import ru.glebik.tinkoff_fintech.feature.user.data.mapper.mapToUser
import ru.glebik.tinkoff_fintech.feature.user.data.mapper.toDomain
import ru.glebik.tinkoff_fintech.feature.user.data.mapper.toEntity
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import java.util.Locale
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,

    private val userDao: UserDao,
    private val userApi: UserApi,
    private val userLongPollService: UserLongPollService,
) : UserRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllUsers(): Flow<ResultWrapper<List<User>>> = flow {
        runCatching {
            withContext(ioDispatcher) { userDao.getAll() }
        }.fold(
            onSuccess = {
                if (it.isNotEmpty()) {
                    val cachedUsers =
                        it.map { userEntity -> userEntity.toDomain() }.sortedBy { it.status }
                    emit(ResultWrapper.Success(cachedUsers))
                }
            },
            onFailure = { emit(wrapError(it)) }
        )

        withContext(ioDispatcher) { userLongPollService.fetchAllUsers() }
            .map { result ->
                when (result) {
                    is ResultWrapper.Failed -> result
                    is ResultWrapper.Success -> {
                        val users = result.data.userResponses ?: listOf()
                        val usersPresence =
                            users
                                //убрал удаленные аккаунты
                                .filter { it.isActive != false }
                                //боты не могут юзать userPresence
                                //"Presence is not supported for bot users."
                                .filter { it.isBot != true }
                                .asFlow()
                                .flatMapMerge {
                                    flow {
                                        val userPresence = userApi.userPresence(it.userId)
                                        emit(it.mapToUser(userPresence.presence, null))
                                    }
                                }
                                .toList()
                                .sortedBy { it.status }

                        userDao.insertAll(usersPresence.map { it.toEntity() })

                        ResultWrapper.Success(usersPresence)
                    }
                }
            }.collect {
                emit(it)
            }
    }

// вариант с подтягиванием всех presence сразу, но тогда половина онлайн, половина оффлайн,
// надо фиксить
//    override suspend fun getAllUsers(): Flow<ResultWrapper<List<User>>> =
//        withContext(ioDispatcher) { userLongPollService.fetchAllUsers() }
//            .map { result ->
//                when (result) {
//                    is ResultWrapper.Failed -> result
//                    is ResultWrapper.Success -> {
//                        runCatching {
//                            userApi.getAllUserPresences()
//                        }.fold(
//                            onSuccess = { mapUsersPresenceResponse ->
//                                val users = result.data.userResponses ?: listOf()
//                                val usersPresence =
//                                    users
//                                        .filter { it.isActive != false }
//                                        .filter { it.isBot != true }
//                                        .map {
//                                            val userPresence =
//                                                mapUsersPresenceResponse.presences[it.email.orEmpty()]
//                                            it.mapToUser(
//                                                userPresence,
//                                                mapUsersPresenceResponse.serverTimestamp.toLong()
//                                            )
//                                        }
//                                        .sortedBy { it.status }
//
//                                cachedUsers = usersPresence
//                                ResultWrapper.Success(usersPresence)
//                            },
//                            onFailure = { ResultWrapper.Failed(it, it.message) }
//                        )
//                    }
//                }
//            }

    override suspend fun getOwnUser(): Flow<ResultWrapper<User>> = flow {
        runCatching {
            withContext(ioDispatcher) { userDao.getById(CURRENT_USER_ID) }
        }.fold(
            onSuccess = {
                if (it != null) {
                    emit(ResultWrapper.Success(it.toDomain()))
                }
            },
            onFailure = { emit(wrapError(it)) }
        )

        withContext(ioDispatcher) { userLongPollService.fetchOwnUser() }.collect {
            emit(it)
            if (it is ResultWrapper.Success) {
                userDao.insert(it.data.toEntity())
            }
        }
    }


    override suspend fun searchUsers(name: String): ResultWrapper<List<User>> =
        withContext(defaultDispatcher) {
            runCatching {
                val cachedUsers =
                    withContext(ioDispatcher) { userDao.getAll().map { it.toDomain() } }
                cachedUsers.filter {
                    it.name.lowercase(Locale.ROOT).contains(name.lowercase(Locale.ROOT))
                }
            }.fold(
                onSuccess = { ResultWrapper.Success(it) },
                onFailure = { wrapError(it) }
            )
        }

    companion object {
        const val CURRENT_USER_EMAIL = "gleb.gafeev@mail.ru"
        const val CURRENT_USER_FULL_NAME = "Gleb Gafeev"
        const val CURRENT_USER_ID = 708825
    }
}