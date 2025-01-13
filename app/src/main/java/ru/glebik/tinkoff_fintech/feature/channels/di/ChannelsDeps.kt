package ru.glebik.tinkoff_fintech.feature.channels.di

import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ru.glebik.core.db.api.dao.StreamDao
import ru.glebik.core.db.api.dao.TopicDao
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.tinkoff_fintech.main.di.BaseUiFeatureDeps

interface ChannelsDeps : BaseUiFeatureDeps {

    @DefaultDispatcherQualifier
    fun defaultDispatcher(): CoroutineDispatcher

    @IoDispatcherQualifier
    fun ioDispatcher(): CoroutineDispatcher

    fun topicDao(): TopicDao

    fun streamDao(): StreamDao

    fun retrofit(): Retrofit

}