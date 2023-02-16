package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.User
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(private val api: Api) {
    suspend fun login(user: User) = api.logIn(user)

    suspend fun signup(user: User) = api.signUp(user)

   // fun log(user: User) = RetrofitInstance.instance.logIn(user)
}