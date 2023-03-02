package com.example.sharebookapp.data.model

data class Category(
    val id: Long,
    val name: String,
    val publications: List<Publication>
): java.io.Serializable