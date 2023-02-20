package com.example.sharebookapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.ioc.component.MainActivityComponent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityComponent = (application as App).appComponent.getMainActivityComponent()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController: View? = findViewById(R.id.bottomFragmentContainer)
        navController?.findNavController()?.let {
            Log.i("Frag", "set up")
            bottomNav.setupWithNavController(it)
        }

    }
}