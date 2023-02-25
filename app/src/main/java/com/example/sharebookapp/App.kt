package com.example.sharebookapp

import android.app.Application
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.ioc.component.AppComponent
import com.example.sharebookapp.ioc.component.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent
    lateinit var currentUser: User
    lateinit var accessToken: String

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}