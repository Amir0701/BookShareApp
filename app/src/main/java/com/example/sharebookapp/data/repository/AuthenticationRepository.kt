package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.ioc.scope.AuthScope
import javax.inject.Inject

@AuthScope
class AuthenticationRepository @Inject constructor(private val api: Api) {
    suspend fun login(user: User) = api.logIn(user)

    suspend fun signup(user: User) = api.signUp(user)
}