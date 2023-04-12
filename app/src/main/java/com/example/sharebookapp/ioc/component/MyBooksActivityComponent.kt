package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.*
import com.example.sharebookapp.ui.view.MyBooksActivity
import dagger.Subcomponent

@Subcomponent
@MyBooksScope
@PublicationRepositoryScope
@CategoryRepositoryScope
@CityRepositoryScope
@ImageRepositoryScope
interface MyBooksActivityComponent {
    fun getMyBooksFragmentComponent(): MyBooksFragmentComponent
    fun inject(myBooksActivity: MyBooksActivity)
}