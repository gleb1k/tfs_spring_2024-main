package ru.glebik.tinkoff_fintech.feature.profile.di

import dagger.Module
import dagger.Provides
import ru.glebik.tinkoff_fintech.feature.profile.ui.vm.ProfileStore
import ru.glebik.tinkoff_fintech.feature.profile.ui.vm.ProfileStoreFactory

@Module
object ProfileModule {

    @Provides
    fun provideProfileStore(
        profileStoreFactory: ProfileStoreFactory,
    ): ProfileStore = profileStoreFactory.create()

}