package com.example.sharebookapp.data.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Publication(
    val id: Long,
    val userId: Long,
    val category: Category,
    @SerializedName("cityDto")
    val city: City,
    val name: String,
    val description: String,
    val publishedAt: Timestamp?,
    @SerializedName("imagesDto")
    val images: List<Image>
): java.io.Serializable
