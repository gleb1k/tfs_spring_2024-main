package ru.glebik.tinkoff_fintech.feature.user.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.user.data.UserRepository
import ru.glebik.tinkoff_fintech.feature.user.domain.model.User
import javax.inject.Inject

class SearchUsersUseCaseImpl @Inject constructor(
    private val repository: UserRepository,
) : SearchUsersUseCase {
    override suspend fun invoke(query: String): ResultWrapper<List<User>> =
        repository.searchUsers(query)
}