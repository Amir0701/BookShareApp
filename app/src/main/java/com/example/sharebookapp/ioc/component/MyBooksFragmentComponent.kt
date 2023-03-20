package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ui.view.MyBooksFragment
import dagger.Subcomponent

@Subcomponent
interface MyBooksFragmentComponent {
    fun inject(myBooksFragment: MyBooksFragment)
}