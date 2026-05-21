package com.example.moviezone.app.data.repository

import com.example.moviezone.app.data.local.MovieDao
import com.example.moviezone.app.data.local.userdata.UserDao
import com.example.moviezone.app.data.local.userdata.UserDetails

class UserRepo(private val dao: UserDao) {
     suspend fun register(name: String, email: String): String {

        val existing = dao.checkEmail(email)

        if (existing != null) {
            return "User already exists"
        }

        val user = UserDetails(
            name = name,
            email = email
        )

        dao.registerUser(user)


        return "Registered Successfully"
    }

    suspend fun login(email: String): UserDetails? {
        return dao.loginUser(email)
    }
    suspend fun getUser(): UserDetails? {
        return dao.getUser()
    }
    suspend fun updateName(name: String, email: String) {
        dao.updateName(name, email)
    }
    suspend fun checkEmail(email: String): UserDetails? {
        return dao.checkEmail(email)
    }

    suspend fun updateProfileImage( uri: String) {
        dao.updateProfileImage( uri)
    }
    suspend fun logout() {
        dao.clearUser()


    }
}