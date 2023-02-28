package com.example.sharebookapp.ui.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.sql.Timestamp
import java.time.LocalDateTime


class NewPublicationFragment : Fragment() {
    private val CHOOSE_IMAGE: Int = 101
    private var chosenImageName: TextView? = null
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addPublicationButton: FloatingActionButton

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
        spinnerCity = view.findViewById(R.id.cityList)
        spinnerCategory = view.findViewById(R.id.categoryList)
        nameEditText = view.findViewById(R.id.nameOfPublication)
        descriptionEditText = view.findViewById(R.id.descriptionPublication)
        addPublicationButton = view.findViewById(R.id.publishButton)

        val choseImageButton:TextView = view.findViewById(R.id.addImageButton)
        choseImageButton.setOnClickListener {
            chooseImage()
        }

        observeCity()
        observeCategory()
        observePublication()
        mainActivityViewModel.getAllCities()
        mainActivityViewModel.getAllCategories()

        addPublicationButton.setOnClickListener {
            val cityId = spinnerCity.selectedItemId
            val chosenCity = mainActivityViewModel.cityResponse.value?.data?.get(cityId.toInt())
            Log.i("city", chosenCity?.name.toString())
            val categoryId = spinnerCategory.selectedItemId
            val chosenCategory = mainActivityViewModel.categoryResponse.value?.data?.get(categoryId.toInt())
            Log.i("category", chosenCategory?.name.toString())

            if (chosenCategory != null && chosenCity != null) {
                publish(chosenCategory, chosenCity)
            }
            else{
                Log.e("chosen", "category or city is null")
            }
        }
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
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerCity.adapter = spinnerAdapter
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }

        })
    }

    private fun observeCategory(){
        mainActivityViewModel.categoryResponse.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let {categories ->
                        val categoriesNames = mutableListOf<String>()
                        categories.forEach {
                            categoriesNames.add(it.name)
                        }

                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            categoriesNames
                        )
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerCategory.adapter = spinnerAdapter
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })
    }

    private fun observePublication(){
        mainActivityViewModel.publication.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Success -> {

                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })
    }

    private fun chooseImage(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "SELECT PHOTO"), CHOOSE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                chosenImageName?.text = data.data?.path
                try {
                    val mm = BufferedReader(InputStreamReader(FileInputStream(data.data?.path)))
                    val mmm = InputStreamReader(FileInputStream(data.data?.path))
                }catch (ex: FileNotFoundException){

                }
            }
        }
    }

    private fun publish(category: Category, city: City){
        val name = nameEditText.text.toString()
        if(name.isEmpty()){
            Toast.makeText(context, "Введите название публикации", Toast.LENGTH_SHORT)
        }
        else{
            val desc = descriptionEditText.text.toString()
            if(desc.isEmpty()){
                Toast.makeText(context, "Введите описание публикации", Toast.LENGTH_SHORT)
            }
            else{
                val newPublication = Publication(
                    0,
                    ((activity as MainActivity).application as App).currentUser.id,
                    category,
                    city,
                    name,
                    desc,
                    null,
                    ArrayList()
                )
                mainActivityViewModel.postPublication(publication = newPublication)
            }
        }
    }
}