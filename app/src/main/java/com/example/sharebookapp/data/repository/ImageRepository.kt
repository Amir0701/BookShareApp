package com.example.sharebookapp.data.repository

import okhttp3.MultipartBody
import javax.inject.Inject

class ImageRepository @Inject constructor(private val api: Api){
    suspend fun postImages(body: Array<MultipartBody>, token: String) = api.postImages(body, token)
}