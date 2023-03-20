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
    val author: String?,
    val description: String,
    val publishedAt: Timestamp?,
    @SerializedName("imagesDto")
    val images: List<Image>
): java.io.Serializable{

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + category.name.hashCode()
        result = 31 * result + city.name.hashCode()
        result = 31 * result + userId.hashCode()
        if (name.isNotEmpty()) {
            result = 31 * result + name.hashCode()
        }
        if (description.isNotEmpty()) {
            result = 31 * result + description.hashCode()
        }
        if(images.isNotEmpty()){
            result = 31 * images.hashCode()
        }

        return result
    }
}
