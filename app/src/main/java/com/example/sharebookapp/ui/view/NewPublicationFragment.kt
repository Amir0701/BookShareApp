package com.example.sharebookapp.ui.view

import android.app.Activity.RESULT_OK
import android.content.ContentResolver.MimeTypeInfo
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.MimeTypeFilter
import androidx.lifecycle.Observer
import com.example.sharebookapp.R
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource
import okhttp3.MultipartReader
import retrofit2.http.Multipart
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.IntStream


class NewPublicationFragment : Fragment() {
    private val CHOOSE_IMAGE: Int = 101
    private var chosenImageName: TextView? = null
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_publication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chosenImageName = view.findViewById(R.id.chosenImageName)
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        spinner = view.findViewById(R.id.cityList)
        val choseImageButton:TextView = view.findViewById(R.id.addImageButton)
        choseImageButton.setOnClickListener {
            chooseImage()
        }
        observeCity()
        mainActivityViewModel.getAllCities()
    }

    private fun observeCity(){
        mainActivityViewModel.cityResponse.observe(viewLifecycleOwner, Observer {resource ->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let { cities ->
                        val str = mutableListOf<String>()
                        cities.forEach{
                            str.add(it.name)
                        }
                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, str)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.adapter = spinnerAdapter
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }

        })
    }

    private fun chooseImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                chosenImageName?.text = data.data?.path
                data.data
                try {
                    val mm = BufferedReader(InputStreamReader(FileInputStream(data.data?.path)))
                    val mmm = InputStreamReader(FileInputStream(data.data?.path))
                }catch (ex: FileNotFoundException){

                }
            }
        }
    }
}