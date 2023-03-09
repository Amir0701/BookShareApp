package com.example.sharebookapp.data.repository

import okhttp3.MultipartBody
import javax.inject.Inject

class ImageRepository @Inject constructor(private val api: Api){
    suspend fun postImages(body: Array<MultipartBody.Part>, publicationId: Long, token: String) =
        api.postImages(body, publicationId, token)
}