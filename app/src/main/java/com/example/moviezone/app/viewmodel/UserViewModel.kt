package com.example.moviezone.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviezone.app.data.local.userdata.UserDataBase
import com.example.moviezone.app.data.local.userdata.UserDetails
import com.example.moviezone.app.data.repository.UserRepo
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UserDataBase.getDatabase(application).userDao()
    private val repo = UserRepo(dao)

    val loginResult = MutableLiveData<UserDetails?>()
    val registerResult = MutableLiveData<String>()

    fun register(name: String, email: String) {
        viewModelScope.launch {
            val result = repo.register(name, email)
            registerResult.postValue(result)
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            val user = repo.login(email)
            loginResult.postValue(user)
        }
    }

    val userData = MutableLiveData<UserDetails?>()

    fun loadUser() {
        viewModelScope.launch {
            val user = repo.getUser()
            userData.postValue(user)
        }
    }

    fun updateName(name: String, email: String) {
        viewModelScope.launch {
            repo.updateName(name, email)
            loadUser()
        }
    }
    fun logout() {
        viewModelScope.launch {
            repo.logout()
            userData.postValue(null) // clear UI
        }
    }
    fun updateEmail(oldEmail: String, newEmail: String) {
        viewModelScope.launch {
            repo.updateEmail(oldEmail, newEmail)
            loadUser()

        }
    }
}