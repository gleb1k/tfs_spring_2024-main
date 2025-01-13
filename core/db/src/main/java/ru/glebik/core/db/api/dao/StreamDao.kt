package ru.glebik.core.db.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.glebik.core.db.api.entity.StreamEntity

@Dao
interface StreamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stream: StreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(streams: List<StreamEntity>)

    @Query("SELECT * FROM streams")
    suspend fun getAll(): List<StreamEntity>

    @Query("SELECT * FROM streams WHERE isSubscribed=${true}")
    suspend fun getSubscribed(): List<StreamEntity>

}