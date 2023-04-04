package com.example.sharebookapp.data.repository

import com.example.sharebookapp.ioc.scope.CategoryRepositoryScope
import com.example.sharebookapp.ioc.scope.MainActivityScope
import javax.inject.Inject


@CategoryRepositoryScope
class CategoryRepository @Inject constructor(private val api: Api) {
    suspend fun getAllCategories(token: String) = api.getAllCategories(token)
}