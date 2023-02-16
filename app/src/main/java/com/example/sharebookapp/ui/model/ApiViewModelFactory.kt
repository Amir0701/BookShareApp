package com.example.sharebookapp.ui.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.data.repository.AuthenticationRepository

class ApiViewModelFactory(val app: Application, val repository: AuthenticationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApiViewModel(app, repository) as T
    }
}