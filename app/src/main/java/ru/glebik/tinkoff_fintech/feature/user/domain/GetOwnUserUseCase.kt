package ru.glebik.tinkoff_fintech.feature.user.domain

import kotlinx.coroutines.flow.Flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User

interface GetOwnUserUseCase {

    suspend operator fun invoke(): Flow<ResultWrapper<User>>
}