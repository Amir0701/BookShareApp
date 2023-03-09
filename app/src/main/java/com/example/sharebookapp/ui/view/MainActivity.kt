package com.example.sharebookapp.ui.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.*
import com.example.sharebookapp.ioc.component.MainActivityComponent
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.ui.model.MainActivityViewModelFactory
import com.example.sharebookapp.util.Resource
import com.example.sharebookapp.util.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityComponent: MainActivityComponent
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var userRepository: UserRepository
    lateinit var cityRepository: CityRepository
    lateinit var categoryRepository: CategoryRepository
    lateinit var publicationRepository: PublicationRepository
    lateinit var imageRepository: ImageRepository
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityComponent = (application as App).appComponent.getMainActivityComponent()
        setContentView(R.layout.activity_main)
        userRepository = mainActivityComponent.getUserRepository()
        cityRepository = mainActivityComponent.getCityRepository()
        categoryRepository = mainActivityComponent.getCategoryRepository()
        publicationRepository = mainActivityComponent.getPublicationRepository()
        imageRepository = mainActivityComponent.getImageRepository()
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE)

        val viewModelFactory = MainActivityViewModelFactory((application as App), userRepository, cityRepository, categoryRepository, publicationRepository, imageRepository)
        mainActivityViewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController: NavController = this.findNavController(R.id.bottomFragmentContainer)
        navController.let {
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