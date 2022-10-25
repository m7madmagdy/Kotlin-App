package com.example.android.db

import com.example.android.models.User

interface LocalRepository {
    suspend fun getUsers():List<User>
    suspend fun deleteUser(user: User)
    suspend fun insertUser(user: User)
}