package com.example.sharebookapp.data.model

import java.sql.Timestamp

data class Publication(
    val id: Long,
    val userId: Long,
    val category: Category,
    val city: City,
    val name: String,
    val description: String,
    val publishedAt: Timestamp,
    val images: List<Image>
): java.io.Serializable
