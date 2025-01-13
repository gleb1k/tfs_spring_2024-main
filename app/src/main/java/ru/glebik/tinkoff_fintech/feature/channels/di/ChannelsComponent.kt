package ru.glebik.tinkoff_fintech.feature.channels.di

import dagger.Component
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment

@FeatureScope
@Component(modules = [ChannelsModule::class], dependencies = [ChannelsDeps::class])
interface ChannelsComponent {
    fun inject(chatFragment: ChannelsFragment)

    @Component.Factory
    interface Factory {
        fun create(channelsDeps: ChannelsDeps): ChannelsComponent
    }
}