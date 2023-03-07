package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.Publication
import javax.inject.Inject

class PublicationRepository @Inject constructor(private val api: Api) {
    suspend fun postPublication(publication: Publication, token: String) = api.postPublication(publication, token)

    suspend fun getAllPublications(token: String) = api.getAllPublications(token)

    suspend fun getFavoritePublication(userId: Long, token: String) = api.getFavoritePublication(userId, token)

    suspend fun getPublicationsByName(name: String, token: String) = api.getPublicationsByName(name, token)
}