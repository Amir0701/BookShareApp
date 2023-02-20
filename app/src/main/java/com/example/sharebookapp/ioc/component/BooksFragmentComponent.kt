package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ui.view.BooksFragment
import dagger.Component
import dagger.Subcomponent

@Subcomponent
interface BooksFragmentComponent {
    fun inject(booksFragment: BooksFragment)
}