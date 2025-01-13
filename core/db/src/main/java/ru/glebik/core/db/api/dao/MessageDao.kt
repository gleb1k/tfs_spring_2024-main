package ru.glebik.core.db.api.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.glebik.core.db.api.entity.MessageEntity
import ru.glebik.core.db.api.entity.MessageWithReactions

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Transaction
    @Query("SELECT * FROM messages WHERE streamName=:streamName AND topicName=:topicName")
    suspend fun getMessagesFromTopic(streamName: String, topicName : String) : List<MessageWithReactions>

    @Transaction
    @Query("SELECT * FROM messages WHERE streamName=:streamName")
    suspend fun getMessages(streamName: String) : List<MessageWithReactions>

    @Delete
    suspend fun deleteAll(messages: List<MessageEntity>)

    @Delete
    suspend fun delete(message:MessageEntity)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteById(messageId: Int)
}