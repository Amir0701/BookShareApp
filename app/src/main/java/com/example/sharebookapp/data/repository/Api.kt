package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.AuthorizationResponse
import com.example.sharebookapp.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @POST("/auth/signup")
    suspend fun signUp(
        @Body
        user: User
    ): Response<AuthorizationResponse>

    @POST("/auth/login")
    suspend fun logIn(
        @Body
        user: User
    ): Response<AuthorizationResponse>

    @GET("/user")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>
}