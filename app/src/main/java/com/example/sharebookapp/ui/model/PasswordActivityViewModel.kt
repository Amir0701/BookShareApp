package com.example.sharebookapp.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.data.model.Password
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PasswordActivityViewModel(val app: App,
                                private val userRepository: UserRepository): AndroidViewModel(app) {
    val user = MutableLiveData<Resource<User>>()

    fun changePassword(password: Password) = viewModelScope.launch{
        user.postValue(Resource.Loading())
        val response = userRepository.changePassword(password, "Bearer ${app.accessToken}")
        user.postValue(responsePassword(response))
    }

    private fun responsePassword(response: Response<User>): Resource<User>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}