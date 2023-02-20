package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.MainActivityScope
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivityComponent {
    fun getBooksFragmentComponent(): BooksFragmentComponent
}