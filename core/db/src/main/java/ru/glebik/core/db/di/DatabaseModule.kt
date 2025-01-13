package ru.glebik.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.glebik.core.db.MainDatabase
import ru.glebik.core.db.api.dao.MessageDao
import ru.glebik.core.db.api.dao.ReactionDao
import ru.glebik.core.db.api.dao.StreamDao
import ru.glebik.core.db.api.dao.TopicDao
import ru.glebik.core.db.api.dao.UserDao
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMainDatabase(
        context: Context
    ): MainDatabase =
        Room.databaseBuilder(
            context,
            MainDatabase::class.java,
            MainDatabase.DATABASE_NAME
        ).build()

    @Provides
    fun provideStreamDao(
        mainDatabase: MainDatabase
    ) : StreamDao = mainDatabase.streamDao()

    @Provides
    fun provideTopicDao(
        mainDatabase: MainDatabase
    ) : TopicDao = mainDatabase.topicDao()

    @Provides
    fun provideMessageDao(
        mainDatabase: MainDatabase
    ) : MessageDao = mainDatabase.messageDao()

    @Provides
    fun provideReactionDao(
        mainDatabase: MainDatabase
    ) : ReactionDao = mainDatabase.reactionDao()

    @Provides
    fun provideUserDao(
        mainDatabase: MainDatabase
    ) : UserDao = mainDatabase.userDao()
}