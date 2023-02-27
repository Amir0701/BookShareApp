package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.CategoryRepository
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.E

class MainActivityViewModel(private val app: App,
                            private val userRepository: UserRepository,
                            private val cityRepository: CityRepository,
                            private val categoryRepository: CategoryRepository): AndroidViewModel(app) {
    val userResponse = MutableLiveData<Resource<User>>()
    val cityResponse = MutableLiveData<Resource<List<City>>>()
    val categoryResponse = MutableLiveData<Resource<List<Category>>>()

    fun getCurrentUser() = viewModelScope.launch(Dispatchers.IO) {
        userResponse.postValue(Resource.Loading())
        val response = userRepository.getCurrentUser("Bearer ${app.accessToken}")
        userResponse.postValue(getUserResponse(response))
    }

    private fun getUserResponse(response: Response<User>): Resource<User>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            throw Exception()
        }
        else{
            return Resource.Error(response.message())
        }
    }

    fun getAllCities() = viewModelScope.launch(Dispatchers.IO) {
        cityResponse.postValue(Resource.Loading())
        val response = cityRepository.getAllCities("Bearer ${app.accessToken}")
        cityResponse.postValue(getAllCitiesResponse(response))
    }

    private fun getAllCitiesResponse(response: Response<List<City>>): Resource<List<City>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            throw Exception()
        }
        else{
            return Resource.Error(response.message())
        }
    }

    fun getAllCategories() = viewModelScope.launch(Dispatchers.IO) {
        categoryResponse.postValue(Resource.Loading())
        val response = categoryRepository.getAllCategories("Bearer ${app.accessToken}")
        categoryResponse.postValue(getAllCategoriesResponse(response))
    }

    private fun getAllCategoriesResponse(response: Response<List<Category>>): Resource<List<Category>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            throw Exception()
        }
        else{
            return Resource.Error(response.message())
        }
    }
}