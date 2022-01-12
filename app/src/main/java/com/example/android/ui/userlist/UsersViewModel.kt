package com.example.android.ui.userlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.model.entity.User
import com.example.android.model.local.LocalRepositoryImp
import com.example.android.model.local.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private var localRepositoryImp: LocalRepositoryImp
    private var usersMutableLiveData = MutableLiveData<List<User>>()
    val usersLiveData: LiveData<List<User>> get() = usersMutableLiveData

    init {
        val database = UserDatabase.getInstance(application)
        localRepositoryImp = LocalRepositoryImp(database)
    }

    fun getUsers() = viewModelScope.launch {
        usersMutableLiveData.postValue(localRepositoryImp.getUsers())
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepositoryImp.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            localRepositoryImp.deleteUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            localRepositoryImp.updateUser(user)
        }
    }
}