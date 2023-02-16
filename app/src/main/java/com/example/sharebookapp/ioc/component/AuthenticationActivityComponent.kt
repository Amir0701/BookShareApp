package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.data.repository.AuthenticationRepository
import dagger.Subcomponent

@Subcomponent
interface AuthenticationActivityComponent {
    fun getRepository(): AuthenticationRepository
}