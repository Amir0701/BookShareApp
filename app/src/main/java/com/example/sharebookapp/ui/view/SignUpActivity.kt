package com.example.sharebookapp.ui.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.AuthenticationRepository
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ApiViewModelFactory
import com.example.sharebookapp.ui.model.ViewModelable
import com.example.sharebookapp.util.Utils

class SignUpActivity : AppCompatActivity(), ViewModelable {
    private var viewModel: ApiViewModel? = null
    private lateinit var repository: AuthenticationRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val signUpActivityComponent = (application as App).appComponent.getSignUpActivityComponent()
        repository = signUpActivityComponent.getRepository()
        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE)
    }

    override fun getViewModel(): ApiViewModel? {
        return viewModel
    }

    fun saveData(email: String, password: String){
        val editor = sharedPreferences.edit()
        editor.putString("Email", email)
        editor.putString("Password", password)
        editor.apply()
    }
}