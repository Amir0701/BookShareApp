package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.MyBooksScope
import com.example.sharebookapp.ui.view.MyBooksActivity
import dagger.Subcomponent

@Subcomponent
@MyBooksScope
interface MyBooksActivityComponent {
    fun getMyBooksFragmentComponent(): MyBooksFragmentComponent
    fun inject(myBooksActivity: MyBooksActivity)
}