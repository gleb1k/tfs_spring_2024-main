package ru.glebik.core.db.api.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.glebik.core.db.api.entity.ReactionEntity

@Dao
interface ReactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reactions: List<ReactionEntity>)

    @Query("SELECT * FROM reactions WHERE messageId=:messageId")
    suspend fun getByMessageId(messageId: Int): List<ReactionEntity>

    @Query("DELETE FROM reactions WHERE messageId = :messageId")
    suspend fun deleteAllReactionsFromMessageById(messageId: Int)

    @Delete
    suspend fun delete(reactions: List<ReactionEntity>)
}
