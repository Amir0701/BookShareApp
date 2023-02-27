package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.data.repository.CategoryRepository
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.ioc.scope.MainActivityScope
import com.example.sharebookapp.ui.view.MainActivity
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivityComponent {
    fun getBooksFragmentComponent(): BooksFragmentComponent
    fun getUserRepository(): UserRepository
    fun getCityRepository(): CityRepository
    fun getCategoryRepository(): CategoryRepository
}