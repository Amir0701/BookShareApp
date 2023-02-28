package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.data.repository.*
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
    fun getPublicationRepository(): PublicationRepository
    fun getImageRepository(): ImageRepository
}