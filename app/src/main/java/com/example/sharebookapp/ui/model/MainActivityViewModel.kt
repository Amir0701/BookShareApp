package com.example.sharebookapp.ui.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.R
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
    val publicationsByGenre = MutableLiveData<Resource<List<Publication>>>()

    fun getCurrentUser() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                userResponse.postValue(Resource.Loading())
                val response = userRepository.getCurrentUser("Bearer ${app.accessToken}")
                userResponse.postValue(getUserResponse(response))
            }else{
                userResponse.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            userResponse.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try {
            if(hasInternetConnection()){
                cityResponse.postValue(Resource.Loading())
                val response = cityRepository.getAllCities("Bearer ${app.accessToken}")
                cityResponse.postValue(getAllCitiesResponse(response))
            }else{
                cityResponse.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            cityResponse.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try {
            if(hasInternetConnection()){
                categoryResponse.postValue(Resource.Loading())
                val response = categoryRepository.getAllCategories("Bearer ${app.accessToken}")
                categoryResponse.postValue(getAllCategoriesResponse(response))
            }else{
                categoryResponse.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            categoryResponse.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try{
            if(hasInternetConnection()){
                this@MainActivityViewModel.publication.postValue(Resource.Loading())
                val response = publicationRepository.postPublication(publication, "Bearer ${app.accessToken}")
                this@MainActivityViewModel.publication.postValue(postPublicationResponse(response))
            }else{
                this@MainActivityViewModel.publication.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            this@MainActivityViewModel.publication.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try{
            if(hasInternetConnection()){
                imageRepository.postImages(multipartBody, publicationId,"Bearer ${app.accessToken}")
            }
        }catch (t: Throwable){

        }
    }

    fun getAllPublications() = viewModelScope.launch(Dispatchers.IO){
        try{
            if(hasInternetConnection()){
                publications.postValue(Resource.Loading())
                val response = publicationRepository.getAllPublications("Bearer ${app.accessToken}")
                publications.postValue(getPublicationsResponse(response))
            }
            else{
                publication.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            publication.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try {
            if(hasInternetConnection()){
                userRepository.addToFavorite(app.currentUser.id, publicationId, "Bearer ${app.accessToken}")
            }
        }catch (t: Throwable){

        }
    }

    fun getFavoritePublication() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                favoritePublication.postValue(Resource.Loading())
                val response = publicationRepository.getFavoritePublication(app.currentUser.id, "Bearer ${app.accessToken}")
                favoritePublication.postValue(responseFavoritePublication(response))
            }else{
                favoritePublication.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            favoritePublication.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
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
        try {
            if(hasInternetConnection()){
                searchPublication.postValue(Resource.Loading())
                val response = publicationRepository.getPublicationsByName(name, "Bearer ${app.accessToken}")
                searchPublication.postValue(responseSearchPublications(response))
            }else{
                searchPublication.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }
        catch (t: Throwable){
            searchPublication.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    private fun responseSearchPublications(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let{
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    fun getPublicationsByGenre(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if(hasInternetConnection()){
                publicationsByGenre.postValue(Resource.Loading())
                val response = publicationRepository.getPublicationsByGenre(id, "Bearer ${app.accessToken}")
                publicationsByGenre.postValue(responsePublicationsByGenre(response))
            }else{
                publicationsByGenre.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        } catch (t: Throwable){
            publicationsByGenre.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    private fun responsePublicationsByGenre(response: Response<List<Publication>>): Resource<List<Publication>>{
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