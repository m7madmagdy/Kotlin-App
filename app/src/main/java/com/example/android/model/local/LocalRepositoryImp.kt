package com.example.android.model.local

import com.example.android.model.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepositoryImp(private val db: UserDatabase) : LocalRepository {

    override suspend fun getUsers() = withContext(Dispatchers.IO) {
        db.userDAO().getUsers()
    }

    override suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDAO().deleteUser(user)
        }
    }

    override suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDAO().insertUser(user)
        }
    }

    override suspend fun updateUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDAO().updateUser(user)
        }
    }
}