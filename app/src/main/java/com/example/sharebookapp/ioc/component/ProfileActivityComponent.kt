package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.ProfileActivityScope
import com.example.sharebookapp.ioc.scope.UserRepositoryScope
import com.example.sharebookapp.ui.view.ProfileActivity
import dagger.Subcomponent

@ProfileActivityScope
@UserRepositoryScope
@Subcomponent
interface ProfileActivityComponent {
    fun inject(profileActivity: ProfileActivity)
}