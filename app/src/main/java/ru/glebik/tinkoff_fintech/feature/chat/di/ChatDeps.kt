package ru.glebik.tinkoff_fintech.feature.chat.di

import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ru.glebik.core.db.api.dao.MessageDao
import ru.glebik.core.db.api.dao.ReactionDao
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.tinkoff_fintech.main.di.BaseUiFeatureDeps

interface ChatDeps : BaseUiFeatureDeps {

    @DefaultDispatcherQualifier
    fun defaultDispatcher(): CoroutineDispatcher

    @IoDispatcherQualifier
    fun ioDispatcher(): CoroutineDispatcher

    fun messageDao(): MessageDao

    fun reactionDao(): ReactionDao

    fun retrofit(): Retrofit

}