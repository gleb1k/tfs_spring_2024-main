package ru.glebik.tinkoff_fintech.feature.chat.di

import dagger.Component
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.ChatFragment

@FeatureScope
@Component(modules = [ChatModule::class], dependencies = [ChatDeps::class])
interface ChatComponent {

    fun inject(chatFragment: ChatFragment)

    @Component.Factory
    interface Factory {
        fun create(chatDeps: ChatDeps): ChatComponent
    }

}