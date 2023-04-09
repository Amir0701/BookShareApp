package com.example.sharebookapp.ui.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.data.repository.CategoryRepository
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.ImageRepository
import com.example.sharebookapp.data.repository.PublicationRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart

class MyBooksViewModel(
    val app: App,
    private val publicationRepository: PublicationRepository,
    private val categoryRepository: CategoryRepository,
    private val cityRepository: CityRepository,
    private val imageRepository: ImageRepository
    ): AndroidViewModel(app) {
     val publications = MutableLiveData<Resource<List<Publication>>>()
    val cityLiveData = MutableLiveData<Resource<List<City>>>()
    val categoryLiveData = MutableLiveData<Resource<List<Category>>>()
    val updatedPublicationLiveDate = MutableLiveData<Resource<Publication>>()

    fun getPublications() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                publications.postValue(Resource.Loading())
                val response = publicationRepository.getPublicationsByUser(app.currentUser.id, "Bearer ${app.accessToken}")
                publications.postValue(getPublicationResponse(response))
            }else{
                publications.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            publications.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    fun getCategories() = viewModelScope.launch(Dispatchers.IO){
        try {
            if(hasInternetConnection()){
                categoryLiveData.postValue(Resource.Loading())
                val response = categoryRepository.getAllCategories("Bearer ${app.accessToken}")
                categoryLiveData.postValue(getCategoriesResponse(response))
            }else{
                categoryLiveData.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            categoryLiveData.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    fun getCities() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                cityLiveData.postValue(Resource.Loading())
                val response = cityRepository.getAllCities("Bearer ${app.accessToken}")
                cityLiveData.postValue(getCitiesResponse(response))
            }else{
                cityLiveData.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            cityLiveData.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    fun updatePublication(publication: Publication) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                updatedPublicationLiveDate.postValue(Resource.Loading())
                val response = publicationRepository.updatePublication(publication, "Bearer ${app.accessToken}")
                updatedPublicationLiveDate.postValue(updatedPublicationResponse(response))
            }else{
                updatedPublicationLiveDate.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            updatedPublicationLiveDate.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    fun postImage(multipartFiles: Array<MultipartBody.Part>, publicationId: Long) = viewModelScope.launch(Dispatchers.IO){
        try {
            if(hasInternetConnection()){
                imageRepository.postImages(multipartFiles, publicationId, "Bearer ${app.accessToken}")
            }
        }catch (t: Throwable){
            Log.e("error", t.message.toString())
        }
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

    private fun updatedPublicationResponse(response: Response<Publication>): Resource<Publication>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}