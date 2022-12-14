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
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.AuthorizationResponse
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.Repository
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ApiViewModelFactory
import com.example.sharebookapp.util.Resource
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val button: TextView by lazy {
        findViewById(R.id.entryButton)
    }

    private val emailEditText: EditText by lazy {
        findViewById(R.id.emailEditText)
    }

    private val password: EditText by lazy {
        findViewById(R.id.passwordEditText)
    }

    private val repository = Repository()
    private var viewModel: ApiViewModel? = null

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.loginProgressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val regLink: TextView = findViewById(R.id.regLink)
        regLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel?.authentication?.observe(this, Observer { it ->
            when(it){
                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    it.data?.let { data ->
                        Log.i("TAG", data.accessToken)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        logIn()
    }

    private fun logIn(){
        button.setOnClickListener {
            if(emailEditText.text.toString().trim() != "" && password.text.toString().trim() != ""){
                val email = emailEditText.text.toString()
                val password = password.text.toString()
                viewModel?.makeRequest(email, password)
            }
            else{
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }


//    private fun logIn(){
//        button.setOnClickListener {
//            if(emailEditText.text.toString().trim() != "" && password.text.toString().trim() != ""){
//                val email = emailEditText.text.toString()
//                val password = password.text.toString()
//                val user = User(0, email, password, "", "" ,ArrayList())
//                val responseAu = repository.log(user)
//                response(responseAu)
//            }
//            else{
//                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}