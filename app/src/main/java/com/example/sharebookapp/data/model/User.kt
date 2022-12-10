package com.example.sharebookapp.data.model

data class User(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String,
    val publications: List<Publication>
)
