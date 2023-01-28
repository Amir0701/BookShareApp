package com.example.sharebookapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.Repository
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ApiViewModelFactory
import com.example.sharebookapp.ui.model.ViewModelable

class AuthenticationActivity : AppCompatActivity(), ViewModelable {
    private var viewModel: ApiViewModel? = null
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_login)

        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]
        Log.i("TAG", "${viewModel.toString()}")

    }


    override fun getViewModel(): ApiViewModel? {
        return viewModel
    }
}