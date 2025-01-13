package ru.glebik.tinkoff_fintech.feature.user.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User

interface SearchUsersUseCase {
    suspend operator fun invoke(query: String): ResultWrapper<List<User>>
}