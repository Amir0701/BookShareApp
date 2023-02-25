package com.example.sharebookapp.data.repository

import javax.inject.Inject

class UserRepository @Inject constructor(private val api: Api) {
    suspend fun getCurrentUser(token: String) = api.getCurrentUser(token)
}