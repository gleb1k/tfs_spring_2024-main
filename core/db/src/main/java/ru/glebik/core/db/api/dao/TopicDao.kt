package ru.glebik.core.db.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.glebik.core.db.api.entity.TopicEntity


@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topic : TopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicEntity>)

    @Query("SELECT * FROM topics WHERE id=:id")
    suspend fun get(id: Int): TopicEntity?

    @Query("SELECT * FROM topics WHERE parentStreamId=:streamId")
    suspend fun getAllTopicsByStreamId(streamId: Int): List<TopicEntity>

    @Query("DELETE FROM topics")
    suspend fun deleteAll()

    @Query("DELETE FROM topics where parentStreamId=:streamId")
    suspend fun deleteAllTopicsFromStream(streamId: Int)

    @Transaction
    suspend fun replaceAll(streamId: Int,topics: List<TopicEntity>) {
        deleteAllTopicsFromStream(streamId)
        insertAll(topics)
    }

}