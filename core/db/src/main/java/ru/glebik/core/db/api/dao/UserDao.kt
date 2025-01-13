package ru.glebik.core.db.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.glebik.core.db.api.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users ")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id=:userId")
    suspend fun getById(userId: Int) : UserEntity?
}
