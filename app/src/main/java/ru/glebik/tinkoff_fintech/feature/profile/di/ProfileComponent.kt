package ru.glebik.tinkoff_fintech.feature.profile.di

import dagger.Component
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.profile.ProfileFragment

@FeatureScope
@Component(modules = [ProfileModule::class], dependencies = [ProfileDeps::class])
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(profileDeps: ProfileDeps): ProfileComponent
    }

}