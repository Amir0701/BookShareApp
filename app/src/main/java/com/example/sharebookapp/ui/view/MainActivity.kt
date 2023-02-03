package com.example.sharebookapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharebookapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController: View? = findViewById(R.id.bottomFragmentContainer)
        navController?.findNavController()?.let {
            Log.i("Frag", "set up")
            bottomNav.setupWithNavController(it)
        }
    }

//    override fun onBackPressed() {
//        //finish()
//    }
}