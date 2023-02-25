package com.example.sharebookapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.UserRepository

class MainActivityViewModelFactory(val app: App,
                                   val userRepository: UserRepository,
                                   val cityRepository: CityRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(app, userRepository, cityRepository) as T
    }
}