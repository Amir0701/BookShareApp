package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.data.repository.PublicationRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MyBooksViewModel(
    val app: App,
    private val publicationRepository: PublicationRepository
    ): AndroidViewModel(app) {
     val publications = MutableLiveData<Resource<List<Publication>>>()

    fun getPublications() = viewModelScope.launch {
        publications.postValue(Resource.Loading())
        val response = publicationRepository.getPublicationsByUser(app.currentUser.id, "Bearer ${app.accessToken}")
        publications.postValue(getPublicationResponse(response))
    }

    private fun getPublicationResponse(response: Response<List<Publication>>): Resource<List<Publication>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}