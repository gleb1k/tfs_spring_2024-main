package ru.glebik.tinkoff_fintech.feature.user.domain

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.data.UserRepository
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import javax.inject.Inject

class GetOwnUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository,
) : GetOwnUserUseCase {

    override suspend fun invoke(): Flow<ResultWrapper<User>> = repository.getOwnUser()
}