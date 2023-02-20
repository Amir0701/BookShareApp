package com.example.sharebookapp.ioc.component

import com.example.sharebookapp.ioc.module.ApiModule
import com.example.sharebookapp.ioc.scope.AppScope
import dagger.Component

@AppScope
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun getAuthenticationActivityComponent(): AuthenticationActivityComponent
    fun getSignUpActivityComponent(): SignUpActivityComponent
    fun getMainActivityComponent(): MainActivityComponent
}