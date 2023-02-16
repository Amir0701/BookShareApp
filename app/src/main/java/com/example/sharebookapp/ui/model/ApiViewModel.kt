package com.example.sharebookapp.ui.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.data.model.AuthorizationResponse
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.AuthenticationRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(val app: Application, val repository: AuthenticationRepository): AndroidViewModel(app) {
    var responseAuthentification: Response<AuthorizationResponse>? = null
    val authentication = MutableLiveData<Resource<AuthorizationResponse>>()

    fun makeRequest(email: String, password: String) = viewModelScope.launch {
        val user = User(0, email, password, "", "" ,ArrayList())
        authentication.postValue(Resource.Loading())
        Log.i("TAG", "Make response")
        val response = repository.login(user)
        authentication.postValue(makeResponse(response))
        Log.i("TAG", "success")
    }

    private fun makeResponse(response: Response<AuthorizationResponse>): Resource<AuthorizationResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            throw Exception()
        }
        else{
            Log.e("TAG", response.message())
            return Resource.Error(response.message())
        }
    }


    fun signUp(user: User) = viewModelScope.launch{
        authentication.postValue(Resource.Loading())
        val response = repository.signup(user)
        authentication.postValue(signUpResponse(response))
    }

    private fun signUpResponse(response: Response<AuthorizationResponse>): Resource<AuthorizationResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
            throw Exception()
        }
        else{
            Log.e("TAG", response.message())
            return Resource.Error(response.message())
        }
    }
}