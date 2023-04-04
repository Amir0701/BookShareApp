package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksFragment : Fragment() {
    @Inject
    lateinit var adapter: BookAdapter
    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var booksRecycler: RecyclerView
    lateinit var searchView: EditText
    lateinit var categoryRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Frag", "Book Fragment")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).mainActivityComponent.getBooksFragmentComponent().inject(this)
        booksRecycler = view.findViewById(R.id.booksRecyclerView)
        searchView = view.findViewById(R.id.booksSearchView)
        categoryRecyclerView = view.findViewById(R.id.genreRecyclerView)

        adapter.setOnBookItemClickListener(object : BookAdapter.OnBookItemClickListener {
            override fun onItemClick(publication: Publication) {
                val bundle = Bundle()
                bundle.putSerializable("choosenPublication", publication)
                findNavController().navigate(R.id.action_booksFragment_to_publicationDetailFragment, bundle)
            }
        })

        initRecyclerView()
        initCategoryRecyclerView()
        var job: Job? = null
        searchView.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let {
                    if(editable.toString().trim().isNotEmpty()){
                        mainActivityViewModel.getPublicationsByName(editable.toString().trim())
                    }
                    else{
                        mainActivityViewModel.getAllPublications()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observePublications()
        mainActivityViewModel.getAllPublications()
        observeSearchPublications()
        observePublicationsByGenre()
    }

    private fun initRecyclerView(){
        booksRecycler.adapter = adapter
        booksRecycler.layoutManager = GridLayoutManager(context, 2)
    }

    private fun initCategoryRecyclerView(){
        val list = mutableListOf<Category>()
        list.add(Category(1, "Роман", ArrayList()))
        list.add(Category(3, "Триллер", ArrayList()))
        list.add(Category(2, "Научная фантастика", ArrayList()))
        list.add(Category(4, "Классика", ArrayList()))
        list.add(Category(5, "Экономика", ArrayList()))
        list.add(Category(6, "Право", ArrayList()))
        categoryAdapter.categoryList = list
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.adapter = categoryAdapter
        categoryAdapter.setOnGenreClickListener(object : CategoryAdapter.OnGenreSelectedItem{
            override fun onGenreItemClick(category: Category, isSelected: Boolean) {
                if(isSelected){
                    mainActivityViewModel.getPublicationsByGenre(category.id)
                }
                else{
                    mainActivityViewModel.getAllPublications()
                }
            }
        })
    }

    private fun observePublications(){
        mainActivityViewModel.searchPublication.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> {
                    Log.i("loading", "All publications are loading")
                }

                is Resource.Success -> {
                    resource.data?.let {
                        adapter.setPublication(it)
                    }
                }

                is Resource.Error -> {
                    Log.e("all publications", resource.message.toString())
                }
            }
        })
    }

    private fun observeSearchPublications(){
        mainActivityViewModel.publications.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> {
                    Log.i("loading", "searching")
                }

                is Resource.Success -> {
                    resource.data?.let {
                        Log.i("succ", "Success search")
                        adapter.setPublication(it)
                    }
                }

                is Resource.Error -> {
                    Log.e("search", resource.message.toString())
                }
            }
        })
    }

    private fun observePublicationsByGenre(){
        mainActivityViewModel.publicationsByGenre.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> Log.i("loading", "publications by genre is loading")

                is Resource.Error -> Log.e("error", resource.message.toString())

                is Resource.Success -> {
                    resource.data?.let {
                        adapter.setPublication(it)
                    }
                }
            }
        })
    }
}