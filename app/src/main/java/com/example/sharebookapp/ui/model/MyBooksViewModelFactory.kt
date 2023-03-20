package com.example.sharebookapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.data.repository.PublicationRepository

class MyBooksViewModelFactory(val app: App,
                              private val publicationRepository: PublicationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyBooksViewModel(app, publicationRepository) as T
    }
}