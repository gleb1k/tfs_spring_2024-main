package ru.glebik.tinkoff_fintech.feature.user.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.glebik.tinkoff_fintech.feature.user.data.UserApi
import ru.glebik.tinkoff_fintech.feature.user.data.UserLongPollService
import ru.glebik.tinkoff_fintech.feature.user.data.UserRepository
import ru.glebik.tinkoff_fintech.feature.user.data.UserRepositoryImpl
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCaseImpl
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCaseImpl


@Module
interface UserModule {

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindGetOwnUserUseCase(getOwnUserUseCaseImpl: GetOwnUserUseCaseImpl): GetOwnUserUseCase

    @Binds
    fun bindGetAllUsersUseCase(getAllUsersUseCaseImpl: GetAllUsersUseCaseImpl): GetAllUsersUseCase

    @Binds
    fun bindSearchUsersUseCase(searchUsersUseCaseImpl: SearchUsersUseCaseImpl): SearchUsersUseCase

    companion object {
        @Provides
        fun provideUserApi(
            retrofit: Retrofit,
        ): UserApi = retrofit.create(UserApi::class.java)

        @Provides
        fun provideUsersLongPollService(
            userApi: UserApi,
        ): UserLongPollService = UserLongPollService(userApi)

    }

}
