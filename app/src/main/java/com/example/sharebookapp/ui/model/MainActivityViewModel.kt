package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.*
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import java.nio.file.DirectoryStream
import kotlin.math.E

class MainActivityViewModel(private val app: App,
                            private val userRepository: UserRepository,
                            private val cityRepository: CityRepository,
                            private val categoryRepository: CategoryRepository,
                            private val publicationRepository: PublicationRepository,
                            private val imageRepository: ImageRepository): AndroidViewModel(app) {
    val userResponse = MutableLiveData<Resource<User>>()
    val cityResponse = MutableLiveData<Resource<List<City>>>()
    val categoryResponse = MutableLiveData<Resource<List<Category>>>()
    var publication = MutableLiveData<Resource<Publication>>()
    val publications = MutableLiveData<Resource<List<Publication>>>()
    val favoritePublication = MutableLiveData<Resource<List<Publication>>>()
    val searchPublication = MutableLiveData<Resource<List<Publication>>>()

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

    fun postPublication(publication: Publication) = viewModelScope.launch(Dispatchers.IO) {
        this@MainActivityViewModel.publication.postValue(Resource.Loading())
        val response = publicationRepository.postPublication(publication, "Bearer ${app.accessToken}")
        this@MainActivityViewModel.publication.postValue(postPublicationResponse(response))
    }

    private fun postPublicationResponse(response: Response<Publication>): Resource<Publication>{
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

    fun postImages(multipartBody: Array<MultipartBody.Part>, publicationId: Long) = viewModelScope.launch(Dispatchers.IO){
        imageRepository.postImages(multipartBody, publicationId,"Bearer ${app.accessToken}")
    }

    fun getAllPublications() = viewModelScope.launch(Dispatchers.IO){
        publications.postValue(Resource.Loading())
        val response = publicationRepository.getAllPublications("Bearer ${app.accessToken}")
        publications.postValue(getPublicationsResponse(response))
    }

    private fun getPublicationsResponse(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            return Resource.Error(response.message())
        }
        else
            return Resource.Error(response.message())
    }

    fun addToFavorite(publicationId: Long) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.addToFavorite(app.currentUser.id, publicationId, "Bearer ${app.accessToken}")
    }

    fun getFavoritePublication() = viewModelScope.launch(Dispatchers.IO) {
        favoritePublication.postValue(Resource.Loading())
        val response = publicationRepository.getFavoritePublication(app.currentUser.id, "Bearer ${app.accessToken}")
        favoritePublication.postValue(responseFavoritePublication(response))
    }

    private fun responseFavoritePublication(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getPublicationsByName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        searchPublication.postValue(Resource.Loading())
        val response = publicationRepository.getPublicationsByName(name, "Bearer ${app.accessToken}")
        searchPublication.postValue(responseSearchPublications(response))
    }

    private fun responseSearchPublications(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let{
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}