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

    @POST("/image/upload")
    @Multipart
    suspend fun postImages(
        @Part body: Array<MultipartBody.Part>,
        @Query("publication_id") publicationId: Long,
        @Header("Authorization") token: String
    )

    @GET("publication/all")
    suspend fun getAllPublications(@Header("Authorization") token: String): Response<List<Publication>>

    @POST("user/favorite")
    suspend fun addToFavorite(
        @Query("userId") userId: Long,
        @Query("publicationId") publicationId: Long,
        @Header("Authorization") token: String)

    @GET("publication/favorite")
    suspend fun getFavoritePublication(
        @Query("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<List<Publication>>

    @GET("publication/search")
    suspend fun getPublicationsByName(
        @Query("name") name: String,
        @Header("Authorization") token: String
    ): Response<List<Publication>>

    @PUT("user/password")
    suspend fun changePassword(
        @Body password: Password,
        @Header("Authorization") token: String
    ): Response<User>

    @PUT("user/edit")
    suspend fun updateUser(
        @Body user: User,
        @Header("Authorization") token: String
    ): Response<User>

    @GET("/publication")
    suspend fun getPublicationByUser(
        @Query("userId") id: Long,
        @Header("Authorization") token: String
    ): Response<List<Publication>>

    @GET("/publication")
    suspend fun getPublicationsByGenre(
        @Query("categoryId") id: Long,
        @Header("Authorization") token: String
    ): Response<List<Publication>>

    @PUT("/publication")
    suspend fun updatePublication(
        @Body publication: Publication,
        @Header("Authorization") token: String
    ): Response<Publication>

    @GET("/user/{id}")
    suspend fun getUserById(
        @Path("id") userId: Long,
        @Header("Authorization") token: String
    ):Response<User>
}