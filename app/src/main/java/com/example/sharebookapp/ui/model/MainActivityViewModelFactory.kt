package com.example.sharebookapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.data.repository.*

class MainActivityViewModelFactory(val app: App,
                                   private val userRepository: UserRepository,
                                   private val cityRepository: CityRepository,
                                   private val categoryRepository: CategoryRepository,
                                   private val publicationRepository: PublicationRepository,
                                   private val imageRepository: ImageRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            app,
            userRepository,
            cityRepository,
            categoryRepository,
            publicationRepository,
            imageRepository
        ) as T
    }
}