package ru.glebik.tinkoff_fintech.feature.profile.di

import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCase
import ru.glebik.tinkoff_fintech.main.di.BaseUiFeatureDeps

interface ProfileDeps : BaseUiFeatureDeps {

    fun getOwnUserUseCase(): GetOwnUserUseCase

}
