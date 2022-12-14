package com.example.sharebookapp.data.model

data class AuthorizationResponse(
    val id: Long,
    val refreshToken: String,
    val accessToken: String
)
