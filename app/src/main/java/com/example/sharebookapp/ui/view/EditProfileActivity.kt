package com.example.sharebookapp.ui.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.data.repository.UserRepository
import com.example.sharebookapp.ui.model.EditProfileViewModel
import com.example.sharebookapp.ui.model.EditProfileViewModelFactory
import com.example.sharebookapp.util.Resource
import com.example.sharebookapp.util.Utils
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: EditProfileViewModel
    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var currentUser: User
    private var isEmailUpdated = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        (application as App).appComponent.getEditProfileActivityComponent().inject(this)
        val viewModelFactory = EditProfileViewModelFactory((application as App), userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[EditProfileViewModel::class.java]
        currentUser = (application as App).currentUser
        sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE)

        val toolbar: Toolbar = findViewById(R.id.editProfileToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            title = "Редактирование профиля"
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        nameEditText = findViewById(R.id.userNameEditText)
        emailEditText = findViewById(R.id.userEmailEditText)
        phoneEditText = findViewById(R.id.userPhoneEditText)
        setUpUser()
        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.edit_done){
            val updatedUser = getUser()
            updatedUser?.let {
                viewModel.updateUser(it)
            }
        }
        return true
    }

    private fun setUpUser(){
        nameEditText.text.append(currentUser.name)
        emailEditText.text.append(currentUser.email)
        phoneEditText.text.append(currentUser.phoneNumber)
    }

    private fun observe(){
        viewModel.updatedUser.observe(this, Observer{ resource ->
            when(resource){
                is Resource.Loading -> Log.i("loading", "update user is loading")
                is Resource.Error -> Log.e("edit", resource.message.toString())
                is Resource.Success -> {
                    resource.data?.let {
                        (application as App).currentUser = it
                        if(isEmailUpdated){
                            sharedPreferences.edit().putString("Email", it.email).apply()
                        }
                        this.finish()
                    }
                }
            }
        })
    }

    private fun getUser(): User?{
        var isEdit = false
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phoneNumber = phoneEditText.text.toString().trim()

        if(name.isNotEmpty() && name != currentUser.name){
            isEdit = true
        }

        if(email.isNotEmpty() && email != currentUser.email){
            isEdit = true
            isEmailUpdated = true
        }

        if(phoneNumber.isNotEmpty() && phoneNumber != currentUser.phoneNumber){
            isEdit = true
        }

        return if(isEdit) {
            User(
                currentUser.id,
                email,
                currentUser.password,
                name,
                phoneNumber,
                currentUser.publications
            )
        } else
            null
    }
}