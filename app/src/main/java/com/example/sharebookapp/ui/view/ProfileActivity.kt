package com.example.sharebookapp.ui.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Password
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.ui.model.PasswordActivityViewModel
import com.example.sharebookapp.ui.model.PasswordActivityViewModelFactory
import com.example.sharebookapp.util.Resource
import com.example.sharebookapp.util.Utils
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {
    private lateinit var passwordActivityViewModel: PasswordActivityViewModel
    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        (application as App).appComponent.getProfileActivityComponent().inject(this)
        val passwordActivityViewModelFactory = PasswordActivityViewModelFactory((application as App), userRepository)
        passwordActivityViewModel = ViewModelProvider(this, passwordActivityViewModelFactory)[PasswordActivityViewModel::class.java]
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE)

        oldPasswordEditText = findViewById(R.id.oldPasswordEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)

        val toolbar: Toolbar = findViewById(R.id.included_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
            it.title = "Изменение пароля"
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.edit_done){
            val password = getPassword()
            password?.let {
                passwordActivityViewModel.changePassword(password)
            }
        }
        return false
    }


    private fun observe(){
        passwordActivityViewModel.user.observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading -> Log.i("loading", "changing password")
                is Resource.Error -> Log.e("pass", resource.message.toString())
                is Resource.Success -> {
                    resource.data?.let {
                        sharedPreferences.edit().putString("Password", newPasswordEditText.text.toString().trim()).apply()
                        this.finish()
                    }
                }
            }
        })
    }

    private fun getPassword(): Password?{
        val old = oldPasswordEditText.text.toString().trim()
        val new = newPasswordEditText.text.toString().trim()
        if(old.isNotEmpty() && new.isNotEmpty()){
            return Password(old, new)
        }
        else {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
        }

        return null
    }
}