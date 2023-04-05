package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.data.repository.CategoryRepository
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.PublicationRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MyBooksViewModel(
    val app: App,
    private val publicationRepository: PublicationRepository,
    private val categoryRepository: CategoryRepository,
    private val cityRepository: CityRepository
    ): AndroidViewModel(app) {
     val publications = MutableLiveData<Resource<List<Publication>>>()
    val cityLiveData = MutableLiveData<Resource<List<City>>>()
    val categoryLiveData = MutableLiveData<Resource<List<Category>>>()

    fun getPublications() = viewModelScope.launch {
        publications.postValue(Resource.Loading())
        val response = publicationRepository.getPublicationsByUser(app.currentUser.id, "Bearer ${app.accessToken}")
        publications.postValue(getPublicationResponse(response))
    }

    fun getCategories() = viewModelScope.launch{
        categoryLiveData.postValue(Resource.Loading())
        val response = categoryRepository.getAllCategories("Bearer ${app.accessToken}")
        categoryLiveData.postValue(getCategoriesResponse(response))
    }

    fun getCities() = viewModelScope.launch {
        cityLiveData.postValue(Resource.Loading())
        val response = cityRepository.getAllCities("Bearer ${app.accessToken}")
        cityLiveData.postValue(getCitiesResponse(response))
    }

    private fun getPublicationResponse(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun getCategoriesResponse(response: Response<List<Category>>): Resource<List<Category>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun getCitiesResponse(response: Response<List<City>>): Resource<List<City>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}