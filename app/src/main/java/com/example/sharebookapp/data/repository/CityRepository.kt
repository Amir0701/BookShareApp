package com.example.sharebookapp.data.repository

import javax.inject.Inject

class CityRepository @Inject constructor(private val api: Api){
    suspend fun getAllCities(token: String) = api.getAllCities(token)
}