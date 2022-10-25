package com.example.android.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.models.User
import com.example.android.db.LocalRepositoryImp
import com.example.android.db.UserDatabase
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

    fun getUsers() = viewModelScope.launch(Dispatchers.IO) {
        usersMutableLiveData.postValue(localRepositoryImp.getUsers())
    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepositoryImp.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepositoryImp.deleteUser(user)
        }
    }
}