package com.example.sharebookapp.ui.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.RealPathUtil
import com.example.sharebookapp.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class NewPublicationFragment : Fragment() {
    private val CHOOSE_IMAGE: Int = 101
    private var chosenImageName: TextView? = null
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addPublicationButton: FloatingActionButton
    private val fileUris = mutableListOf<Uri>()

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
//            val cityId = spinnerCity.selectedItemId
//            val chosenCity = mainActivityViewModel.cityResponse.value?.data?.get(cityId.toInt())
//            Log.i("city", chosenCity?.name.toString())
//            val categoryId = spinnerCategory.selectedItemId
//            val chosenCategory = mainActivityViewModel.categoryResponse.value?.data?.get(categoryId.toInt())
//            Log.i("category", chosenCategory?.name.toString())
//
//            if (chosenCategory != null && chosenCity != null) {
//                publish(chosenCategory, chosenCity)
//            }
//            else{
//                Log.e("chosen", "category or city is null")
//            }

            postImages()
            Log.i("image", "Success")
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
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "SELECT PHOTO"), CHOOSE_IMAGE)
        } else {
            ActivityCompat.requestPermissions(
                (activity as MainActivity),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                chosenImageName?.text = data.data?.path
                data.data?.let { fileUris.add(it)
                Log.i("uri", it.toString())}
                val clipData = data.clipData
//                for (i in 0 until clipData?.itemCount!!){
//                    val item: ClipData.Item = clipData.getItemAt(i)
//                    val uri = item.uri
//                    fileUris.add(uri)
//                }
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

    private fun postImages(){
        val parts: MutableList<MultipartBody.Part> = mutableListOf()
        for (i in 0 until fileUris.size){
            //val file = com.example.sharebookapp.util.FileUtils.getFile(requireContext(), fileUris[i])
            val file = File(RealPathUtil.getRealPath(requireContext(), fileUris[i]))
            val multipartFile = MultipartBody.Part.createFormData(
                "files",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
            parts.add(multipartFile)
        }

        mainActivityViewModel.postImages(parts.toTypedArray(), 139)
    }
}