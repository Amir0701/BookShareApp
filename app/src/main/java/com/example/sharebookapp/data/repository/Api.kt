package com.example.sharebookapp.data.repository

import retrofit2.http.POST

interface Api {
    @POST("/auth/signup")
    suspend fun signUp(

    )
}