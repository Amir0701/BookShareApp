package com.example.sharebookapp.ui.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.AuthenticationRepository
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ApiViewModelFactory
import com.example.sharebookapp.util.Resource
import com.example.sharebookapp.util.Utils

class AuthenticationActivity : AppCompatActivity() {
    private var viewModel: ApiViewModel? = null

    private lateinit var repository: AuthenticationRepository

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_login)
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE)
        val authenticationActivityComponent = (application as App).appComponent.getAuthenticationActivityComponent()
        repository = authenticationActivityComponent.getRepository()
        val viewModelFactory = ApiViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]
        val email: String? = sharedPreferences.getString("Email", "")
        if(email != null && email.isNotEmpty()){
            Log.i("TAG", "Email is not empty")
            val password: String? = sharedPreferences.getString("Password", "")
            if (password != null && password.isNotEmpty()) {
                Log.i("TAG", "Password is not empty")
                viewModel?.authentication?.observe(this, Observer { it ->
                    when(it){
                        is Resource.Success -> {
                            //progressBar.visibility = View.INVISIBLE
                            it.data?.let { data ->
                                Log.i("TAG", "Succ")
                                Log.i("TAG", data.accessToken)
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        is Resource.Loading -> {
                            Log.i("TAG", "Loading")
                            //progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Error -> {
                            //progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                viewModel?.makeRequest(email, password)
            }
        }else{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }


//    override fun getViewModel(): ApiViewModel? {
//        return viewModel
//    }
}