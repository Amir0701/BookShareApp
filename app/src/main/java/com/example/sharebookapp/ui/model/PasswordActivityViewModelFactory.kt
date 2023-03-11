package com.example.sharebookapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sharebookapp.App
import com.example.sharebookapp.data.repository.UserRepository

class PasswordActivityViewModelFactory(val app: App,
                                       private val userRepository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PasswordActivityViewModel(app, userRepository) as T
    }
}