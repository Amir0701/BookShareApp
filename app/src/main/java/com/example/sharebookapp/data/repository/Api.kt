package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.*
import okhttp3.MultipartBody
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

    @GET("/city/all")
    suspend fun getAllCities(@Header("Authorization") token: String): Response<List<City>>

    @GET("/category/all")
    suspend fun getAllCategories(@Header("Authorization") token: String): Response<List<Category>>

    @POST("/publication")
    suspend fun postPublication(
        @Body publication: Publication,
        @Header("Authorization") token: String
    ): Response<Publication>

    @POST("/image")
    @Multipart
    suspend fun postImages(
        @Part body: Array<MultipartBody>,
        @Header("Authorization") token: String
    )
}