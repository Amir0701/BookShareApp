package com.example.sharebookapp.data.repository

import com.example.sharebookapp.data.model.Password
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.ioc.scope.EditProfileActivityScope
import com.example.sharebookapp.ioc.scope.MainActivityScope
import com.example.sharebookapp.ioc.scope.ProfileActivityScope
import com.example.sharebookapp.ioc.scope.UserRepositoryScope
import javax.inject.Inject

@UserRepositoryScope
class UserRepository @Inject constructor(private val api: Api) {
    suspend fun getCurrentUser(token: String) = api.getCurrentUser(token)
    suspend fun addToFavorite(userId: Long, publicationId: Long, token: String) =
        api.addToFavorite(userId, publicationId, token)

    suspend fun changePassword(password: Password, token: String) = api.changePassword(password, token)

    suspend fun updateUser(user: User, token: String) = api.updateUser(user, token)
}