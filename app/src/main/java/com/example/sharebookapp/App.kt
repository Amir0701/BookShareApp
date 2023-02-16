package com.example.sharebookapp

import android.app.Application
import com.example.sharebookapp.ioc.component.AppComponent
import com.example.sharebookapp.ioc.component.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}