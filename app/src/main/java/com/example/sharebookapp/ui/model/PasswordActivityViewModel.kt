package com.example.sharebookapp.ui.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Password
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PasswordActivityViewModel(val app: App,
                                private val userRepository: UserRepository): AndroidViewModel(app) {
    val user = MutableLiveData<Resource<User>>()

    fun changePassword(password: Password) = viewModelScope.launch(Dispatchers.IO){
        try {
            if(hasInternetConnection()){
                user.postValue(Resource.Loading())
                val response = userRepository.changePassword(password, "Bearer ${app.accessToken}")
                user.postValue(responsePassword(response))
            }else{
                user.postValue(Resource.Error(app.resources.getString(R.string.no_connection)))
            }
        }catch (t: Throwable){
            user.postValue(Resource.Error(app.resources.getString(R.string.error_in_connection)))
        }
    }

    private fun responsePassword(response: Response<User>): Resource<User>{
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
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}