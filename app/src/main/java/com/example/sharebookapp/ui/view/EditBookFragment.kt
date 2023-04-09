package com.example.sharebookapp.ui.view

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MyBooksViewModel
import com.example.sharebookapp.util.RealPathUtil
import com.example.sharebookapp.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class EditBookFragment : Fragment() {
    private val args: EditBookFragmentArgs by navArgs()
    private lateinit var nameEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var viewModel: MyBooksViewModel
    private lateinit var publishButton: FloatingActionButton
    private lateinit var chooseImageButton: TextView
    private lateinit var publication: Publication

    private val CHOOSE_IMAGE: Int = 101
    private val fileUris = mutableListOf<Uri>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEditText = view.findViewById(R.id.nameOfPublication)
        authorEditText = view.findViewById(R.id.authorOfPublication)
        descriptionEditText = view.findViewById(R.id.descriptionPublication)
        citySpinner = view.findViewById(R.id.cityList)
        categorySpinner = view.findViewById(R.id.categoryList)
        publishButton = view.findViewById(R.id.publishButton)
        chooseImageButton = view.findViewById(R.id.addImageButton)

        publication = args.currentMyBook
        nameEditText.setText(publication.name)
        descriptionEditText.setText(publication.description)
        publication.author?.let {
            authorEditText.setText(it)
        }

        viewModel = (activity as MyBooksActivity).myBooksViewModel
        observeCity()
        observeCategory()
        observeUpdatedPublication()
        viewModel.getCategories()
        viewModel.getCities()

        publishButton.setOnClickListener {
            getPublication()?.let {
                viewModel.updatePublication(it)
            }
        }
    }

    private fun observeCategory(){
        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {resource ->
            when(resource){
                is Resource.Loading ->{}
                is Resource.Error -> Log.e("error", resource.message.toString())
                is Resource.Success ->{
                    resource.data?.let {categories ->
                        val categoriesNames = mutableListOf<String>()
                        var position = 0
                        var i = 0
                        categories.forEach {
                            categoriesNames.add(it.name)
                            if(args.currentMyBook.category.name == it.name)
                                position = i

                            i++
                        }

                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            categoriesNames
                        )
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        categorySpinner.adapter = spinnerAdapter
                        categorySpinner.setSelection(position)
                    }
                }
            }
        })
    }

    private fun observeCity(){
        viewModel.cityLiveData.observe(viewLifecycleOwner, Observer {resource ->
            when(resource){
                is Resource.Loading ->{}
                is Resource.Error -> Log.e("error", resource.message.toString())
                is Resource.Success ->{
                    resource.data?.let { cities ->
                        val str = mutableListOf<String>()
                        var position = 0
                        var i = 0
                        cities.forEach{
                            str.add(it.name)
                            if(it.name == args.currentMyBook.city.name)
                                position = i

                            i++
                        }
                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, str)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        citySpinner.adapter = spinnerAdapter

                        citySpinner.setSelection(position)
                    }
                }
            }
        })
    }

    private fun observeUpdatedPublication(){
        viewModel.updatedPublicationLiveDate.observe(viewLifecycleOwner, Observer { resources->
            when(resources){
                is Resource.Error -> Log.e("error", resources.message.toString())
                is Resource.Success -> {
                    resources.data?.let {
                        postImage(it.id)
                        findNavController().navigate(R.id.action_editBookFragment_to_myBooksFragment)
                    }
                }
                is Resource.Loading -> Log.i("loading", "publication is updating")
            }
        })
    }

    private fun getPublication(): Publication?{
        val name = getNameText()
        val author = getAuthorText()
        val description = getDescription()
        val city = getCity()
        val category = getCategory()

        if(name != null && description != null && category != null && city != null) {
            return Publication(
                publication.id,
                publication.userId,
                category,
                city,
                name,
                author,
                description,
                publication.publishedAt,
                publication.images,
                publication.publicationImages
            )
        }
        return null
    }

    private fun getNameText(): String?{
        if(nameEditText.text.toString().isNotEmpty()){
            return nameEditText.text.toString()
        }
        return null
    }

    private fun getAuthorText(): String?{
        if(authorEditText.text.toString().isNotEmpty()){
            return authorEditText.text.toString()
        }
        return null
    }

    private fun getCity(): City? {
        val id = citySpinner.selectedItemId.toInt()
        val city = viewModel.cityLiveData.value?.data?.let {
            it[id]
        }

        return city
    }

    private fun getCategory(): Category?{
        val id = categorySpinner.selectedItemId.toInt()
        val category = viewModel.categoryLiveData.value?.data?.let {
            it[id]
        }

        return category
    }

    private fun getDescription(): String?{
        if(descriptionEditText.text.toString().isNotEmpty()){
            return descriptionEditText.text.toString()
        }

        return null
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
        if(requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK){
            if(data != null){
                val index: Int? = data.data?.path?.lastIndexOf('/')
                val clipData = data.clipData
                for (i in 0 until clipData?.itemCount!!){
                    val item: ClipData.Item = clipData.getItemAt(i)
                    val uri = item.uri
                    fileUris.add(uri)
                }
            }
        }
    }

    private fun postImage(publicationId: Long){
        val parts: MutableList<MultipartBody.Part> = mutableListOf()
        for (i in 0 until fileUris.size){
            val file = File(RealPathUtil.getRealPath(requireContext(), fileUris[i]))
            val multipartFile = MultipartBody.Part.createFormData(
                "files",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
            parts.add(multipartFile)
        }

        viewModel.postImage(parts.toTypedArray(), publicationId)
    }
}