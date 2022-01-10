package com.example.android.model.local

import androidx.room.*
import com.example.android.model.entity.User

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from user_table")
    suspend fun getUsers():List<User>
}