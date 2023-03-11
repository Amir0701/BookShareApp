package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.ProfileActivityScope
import com.example.sharebookapp.ui.view.ProfileActivity
import dagger.Subcomponent

@ProfileActivityScope
@Subcomponent
interface ProfileActivityComponent {
    fun inject(profileActivity: ProfileActivity)
}