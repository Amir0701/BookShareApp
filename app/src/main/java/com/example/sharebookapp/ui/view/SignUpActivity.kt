package com.example.sharebookapp.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.Repository
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ApiViewModelFactory
import com.example.sharebookapp.util.Resource

class SignUpActivity : AppCompatActivity() {
    private var viewModel: ApiViewModel? = null
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

    }
}