package com.example.android.model.local

import com.example.android.model.entity.User

interface LocalRepository {
    suspend fun getUsers():List<User>
    suspend fun deleteUser(user: User)
    suspend fun insertUser(user: User)
}