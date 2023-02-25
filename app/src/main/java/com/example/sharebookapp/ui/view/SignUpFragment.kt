package com.example.sharebookapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.User
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ViewModelable
import com.example.sharebookapp.util.Resource


class SignUpFragment : Fragment() {
    private val emailEditText: EditText by lazy {
        requireView().findViewById(R.id.signupEmailEditText)
    }

    private val nameEditText: EditText by lazy {
        requireView().findViewById(R.id.signupNameEditText)
    }

    private val passwordEditText: EditText by lazy {
        requireView().findViewById(R.id.signupPasswordEditText)
    }

    private val phoneNumber: EditText by lazy {
        requireView().findViewById(R.id.signupPhoneNumberEditText)
    }


    private val signUpButton: TextView by lazy{
        requireView().findViewById(R.id.signupButton)
    }

    private val progressBar: ProgressBar by lazy {
        requireView().findViewById(R.id.signUpProgressBar)
    }

    private var viewModel: ApiViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ViewModelable).getViewModel()
        val login: TextView = view.findViewById(R.id.loginLink)
        login.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }

        viewModel?.authentication?.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    it.data?.let { response ->
                        Log.i("TAG", response.accessToken)
                        ((activity as SignUpActivity).application as App).accessToken = response.accessToken
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}