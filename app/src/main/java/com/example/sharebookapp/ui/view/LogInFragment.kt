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
import com.example.sharebookapp.ui.model.ApiViewModel
import com.example.sharebookapp.ui.model.ViewModelable
import com.example.sharebookapp.util.Resource

class LogInFragment : Fragment() {
    private val button: TextView by lazy {
        requireView().findViewById(R.id.entryButton)
    }

    private val emailEditText: EditText by lazy {
        requireView().findViewById(R.id.emailEditText)
    }

    private val password: EditText by lazy {
        requireView().findViewById(R.id.passwordEditText)
    }

    private val progressBar: ProgressBar by lazy {
        requireView().findViewById(R.id.loginProgressBar)
    }

    private var viewModel: ApiViewModel? = null

    private var email: String? = null
    private var pass: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val regLink: TextView = view.findViewById(R.id.regLink)
        regLink.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        logIn()
    }


    override fun onStart() {
        super.onStart()
        viewModel = (activity as ViewModelable).getViewModel()
        Log.i("TAG", "fragment ${viewModel.toString()}")
        viewModel?.authentication?.observe(viewLifecycleOwner, Observer { it ->
            when(it){
                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    it.data?.let { data ->
                        Log.i("TAG", data.accessToken)
                        ((activity as SignUpActivity).application as App).accessToken = data.accessToken
                        val intent = Intent(activity, MainActivity::class.java)
                        if(email != null && pass != null){
                            Log.i("TAG", "Save data")
                            (activity as SignUpActivity).saveData(email!!, pass!!)
                        }
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun logIn(){
        button.setOnClickListener {
            Log.i("TAG", "On log in click")
            if(emailEditText.text.toString().trim() != "" && password.text.toString().trim() != ""){
                email = emailEditText.text.toString()
                pass = password.text.toString()
                Log.i("TAG", "Before request")
                val res = viewModel?.makeRequest(email!!, pass!!)
                Log.i("TAG", res.toString())
            }
            else{
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}