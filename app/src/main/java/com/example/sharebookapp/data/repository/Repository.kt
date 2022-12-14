package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.User

class Repository {
    suspend fun login(user: User) = RetrofitInstance.instance.logIn(user)

    suspend fun signup(user: User) = RetrofitInstance.instance.signUp(user)

   // fun log(user: User) = RetrofitInstance.instance.logIn(user)
}