package com.example.sharebookapp.data.repository

import javax.inject.Inject

class CategoryRepository @Inject constructor(private val api: Api) {
    suspend fun getAllCategories(token: String) = api.getAllCategories(token)
}