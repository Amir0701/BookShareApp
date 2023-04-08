package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ioc.scope.MainActivityScope
import com.example.sharebookapp.ioc.scope.MyBooksScope
import com.example.sharebookapp.ioc.scope.PublicationRepositoryScope
import javax.inject.Inject

@PublicationRepositoryScope
class PublicationRepository @Inject constructor(private val api: Api) {
    suspend fun postPublication(publication: Publication, token: String) = api.postPublication(publication, token)

    suspend fun getAllPublications(token: String) = api.getAllPublications(token)

    suspend fun getFavoritePublication(userId: Long, token: String) = api.getFavoritePublication(userId, token)

    suspend fun getPublicationsByName(name: String, token: String) = api.getPublicationsByName(name, token)

    suspend fun getPublicationsByUser(id: Long, token: String) = api.getPublicationByUser(id, token)

    suspend fun getPublicationsByGenre(id: Long, token: String) = api.getPublicationsByGenre(id, token)

    suspend fun updatePublication(publication: Publication, token: String) = api.updatePublication(publication, token)
}