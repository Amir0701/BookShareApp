package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ui.view.ProfileFragment
import dagger.Subcomponent

@Subcomponent
interface ProfileFragmentComponent {
    fun inject(profileFragment: ProfileFragment)
}