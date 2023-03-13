package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.scope.EditProfileActivityScope
import com.example.sharebookapp.ui.view.EditProfileActivity
import dagger.Subcomponent

@Subcomponent
@EditProfileActivityScope
interface EditProfileActivityComponent {
    fun inject(editProfileActivity: EditProfileActivity)
}