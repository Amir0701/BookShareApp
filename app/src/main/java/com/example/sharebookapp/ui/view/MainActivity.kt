package com.example.sharebookapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.ioc.component.MainActivityComponent
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.ui.model.MainActivityViewModelFactory
import com.example.sharebookapp.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityComponent: MainActivityComponent
    private lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityComponent = (application as App).appComponent.getMainActivityComponent()
        setContentView(R.layout.activity_main)
        userRepository = mainActivityComponent.getUserRepository()
        val viewModelFactory = MainActivityViewModelFactory((application as App), userRepository)
        mainActivityViewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController: View? = findViewById(R.id.bottomFragmentContainer)
        navController?.findNavController()?.let {
            Log.i("Frag", "set up")
            bottomNav.setupWithNavController(it)
        }
        observe()
        mainActivityViewModel.getCurrentUser()
    }

    private fun observe(){
        mainActivityViewModel.userResponse.observe(this, Observer { resourse->
            when(resourse){
                is Resource.Success -> {
                    resourse.data?.let { user ->
                        (application as App).currentUser = user
                        Log.i("user", user.email)
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Log.e("user", resourse.message.toString())
                }
            }
        })
    }
}