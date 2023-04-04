package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.data.repository.*
import com.example.sharebookapp.ioc.scope.*
import com.example.sharebookapp.ui.view.FavoritesFragment
import com.example.sharebookapp.ui.view.MainActivity
import dagger.Subcomponent

@MainActivityScope
@UserRepositoryScope
@PublicationRepositoryScope
@CategoryRepositoryScope
@CityRepositoryScope
@ImageRepositoryScope
@Subcomponent
interface MainActivityComponent {
    fun getBooksFragmentComponent(): BooksFragmentComponent
    fun getFavoritesFragmentComponent(): FavoritesFragmentComponent
    fun getProfileFragmentComponent(): ProfileFragmentComponent
    fun getUserRepository(): UserRepository
    fun getCityRepository(): CityRepository
    fun getCategoryRepository(): CategoryRepository
    fun getPublicationRepository(): PublicationRepository
    fun getImageRepository(): ImageRepository
}