package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.data.repository.AuthenticationRepository
import com.example.sharebookapp.ioc.scope.AuthScope
import dagger.Subcomponent

@AuthScope
@Subcomponent
interface SignUpActivityComponent {
    fun getRepository(): AuthenticationRepository
}