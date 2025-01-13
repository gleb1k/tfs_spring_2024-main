package ru.glebik.tinkoff_fintech.feature.people.di

import dagger.Component
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.people.PeopleFragment

@FeatureScope
@Component(modules = [PeopleModule::class], dependencies = [PeopleDeps::class])
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)

    @Component.Factory
    interface Factory {
        fun create(peopleDeps: PeopleDeps): PeopleComponent
    }

}