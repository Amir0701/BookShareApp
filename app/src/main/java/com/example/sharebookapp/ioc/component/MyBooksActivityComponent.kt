package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.CategoryRepositoryScope
import com.example.sharebookapp.ioc.scope.CityRepositoryScope
import com.example.sharebookapp.ioc.scope.MyBooksScope
import com.example.sharebookapp.ioc.scope.PublicationRepositoryScope
import com.example.sharebookapp.ui.view.MyBooksActivity
import dagger.Subcomponent

@Subcomponent
@MyBooksScope
@PublicationRepositoryScope
@CategoryRepositoryScope
@CityRepositoryScope
interface MyBooksActivityComponent {
    fun getMyBooksFragmentComponent(): MyBooksFragmentComponent
    fun inject(myBooksActivity: MyBooksActivity)
}