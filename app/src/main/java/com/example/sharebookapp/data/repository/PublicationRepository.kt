package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.Publication
import javax.inject.Inject

class PublicationRepository @Inject constructor(private val api: Api) {
    suspend fun postPublication(publication: Publication, token: String) = api.postPublication(publication, token)
}