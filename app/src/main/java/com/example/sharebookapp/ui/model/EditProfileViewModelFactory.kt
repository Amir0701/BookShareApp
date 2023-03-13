package com.example.sharebookapp.ui.model

import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.data.repository.UserRepository

class EditProfileViewModelFactory(val app: App,
                                  private val userRepository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditProfileViewModel(app, userRepository) as T
    }
}