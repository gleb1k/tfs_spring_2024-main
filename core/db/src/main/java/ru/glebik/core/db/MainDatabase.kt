package ru.glebik.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.glebik.core.db.MainDatabase.Companion.DATABASE_VERSION
import ru.glebik.core.db.api.dao.MessageDao
import ru.glebik.core.db.api.dao.ReactionDao
import ru.glebik.core.db.api.dao.StreamDao
import ru.glebik.core.db.api.dao.TopicDao
import ru.glebik.core.db.api.dao.UserDao
import ru.glebik.core.db.api.entity.MessageEntity
import ru.glebik.core.db.api.entity.ReactionEntity
import ru.glebik.core.db.api.entity.StreamEntity
import ru.glebik.core.db.api.entity.TopicEntity
import ru.glebik.core.db.api.entity.UserEntity

@Database(
    version = DATABASE_VERSION,
    entities = [
        StreamEntity::class,
        TopicEntity::class,
        MessageEntity::class,
        ReactionEntity::class,
        UserEntity::class,
    ],
    autoMigrations = []
)

abstract class MainDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao

    abstract fun streamDao(): StreamDao

    abstract fun messageDao(): MessageDao

    abstract fun reactionDao(): ReactionDao

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "main_database"
    }
}
