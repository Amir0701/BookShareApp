package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class EditProfileViewModel(val app: App,
                           private val userRepository: UserRepository): AndroidViewModel(app){
    val updatedUser = MutableLiveData<Resource<User>>()

    fun updateUser(user: User) = viewModelScope.launch {
        updatedUser.postValue(Resource.Loading())
        val response = userRepository.updateUser(user, "Bearer ${app.accessToken}")
        updatedUser.postValue(responseUpdate(response))
    }

    private fun responseUpdate(response: Response<User>): Resource<User>{
        if(response.isSuccessful){
            response.body()?.let{
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}