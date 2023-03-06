package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ui.view.FavoritesFragment
import dagger.Subcomponent

@Subcomponent
interface FavoritesFragmentComponent {
    fun inject(favoritesFragment: FavoritesFragment)
}