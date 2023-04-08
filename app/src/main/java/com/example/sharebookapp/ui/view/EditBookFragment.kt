package com.example.sharebookapp.ui.view

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.City
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MyBooksViewModel
import com.example.sharebookapp.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
        viewModel.getCategories()
        viewModel.getCities()

        publishButton.setOnClickListener {
            getPublication()?.let {
                viewModel.updatePublication(it)
                findNavController().navigate(R.id.action_editBookFragment_to_myBooksFragment)
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
}