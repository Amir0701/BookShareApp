package com.example.sharebookapp.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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
import kotlin.math.log

class SignUpActivity : AppCompatActivity() {
    private val emailEditText: EditText by lazy {
        findViewById(R.id.signupEmailEditText)
    }

    private val nameEditText: EditText by lazy {
        findViewById(R.id.signupNameEditText)
    }

    private val passwordEditText: EditText by lazy {
        findViewById(R.id.signupPasswordEditText)
    }

    private val phoneNumber: EditText by lazy {
        findViewById(R.id.signupPhoneNumberEditText)
    }


    private val signUpButton: TextView by lazy{
        findViewById(R.id.signupButton)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.signUpProgressBar)
    }

    private var viewModel: ApiViewModel? = null
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val login: TextView = findViewById(R.id.loginLink)
        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel?.authentication?.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        Log.i("TAG", it.accessToken)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
            }
        })

        signUp()
    }


    private fun signUp(){
        signUpButton.setOnClickListener {
            if(emailEditText.text.toString().trim() != ""
                && nameEditText.text.toString().trim() != ""
                && passwordEditText.text.toString().trim() != ""
                && phoneNumber.text.toString().trim() != ""){

                val user = User(0,
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    nameEditText.text.toString(),
                    phoneNumber.text.toString(),
                    ArrayList()
                )

                viewModel?.signUp(user)
            }
            else{
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

}