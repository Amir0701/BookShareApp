package com.example.sharebookapp.data.repository

import com.example.sharebookapp.ioc.scope.CityRepositoryScope
import com.example.sharebookapp.ioc.scope.MainActivityScope
import javax.inject.Inject

@CityRepositoryScope
class CityRepository @Inject constructor(private val api: Api){
    suspend fun getAllCities(token: String) = api.getAllCities(token)
}