package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksFragment : Fragment() {
    @Inject
    lateinit var adapter: BookAdapter

    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var booksRecycler: RecyclerView
    lateinit var searchView: EditText
    lateinit var genreChipGroup: ChipGroup
    private lateinit var filterButton: ImageView

    private val args: BooksFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).mainActivityComponent.getBooksFragmentComponent().inject(this)
        booksRecycler = view.findViewById(R.id.booksRecyclerView)
        searchView = view.findViewById(R.id.booksSearchView)
        genreChipGroup = view.findViewById(R.id.genreChipGroup)
        filterButton = view.findViewById(R.id.filterImageView)
        filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_filterFragment)
        }
        adapter.setOnBookItemClickListener(object : BookAdapter.OnBookItemClickListener {
            override fun onItemClick(publication: Publication) {
                val bundle = Bundle()
                bundle.putSerializable("choosenPublication", publication)
                findNavController().navigate(R.id.action_booksFragment_to_publicationDetailFragment, bundle)
            }
        })

        initRecyclerView()
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

        genreChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.let {
                if(it.size > 0){
                    val id = it[0]
                    var chipText = ""
                    group.children.forEach { v->
                        if(v.id == id){
                            chipText = (v as Chip).text.toString()
                        }
                    }

                    var catId: Long = 0
                    mainActivityViewModel.categoryResponse.value?.data?.forEach { cat->
                        if(cat.name == chipText)
                            catId = cat.id
                    }

                    mainActivityViewModel.getPublicationsByGenre(catId)
                }else
                    mainActivityViewModel.getAllPublications()
            }
        }

        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observePublications()
        if(!args.cityName.equals("null")){
            mainActivityViewModel.getPublicationsByCity(args.cityName!!)
        }
        else
            mainActivityViewModel.getAllPublications()

        mainActivityViewModel.getAllCategories()
        observeSearchPublications()
        observePublicationsByGenre()
        observeCategory()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun createChip(genreName: String){
        val themedContext = ContextThemeWrapper(context, R.style.MyChip)
        val chip = Chip(themedContext)
        chip.text = genreName
        chip.isCheckable = true
        genreChipGroup.addView(chip)
    }

    private fun initRecyclerView(){
        booksRecycler.adapter = adapter
        booksRecycler.layoutManager = GridLayoutManager(context, 2)
    }

    private fun observePublications(){
        mainActivityViewModel.publications.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> {
                    Log.i("loading", "All publications are loading")
                }

                is Resource.Success -> {
                    resource.data?.let {
                        Log.i("getpub", "Succ")
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
        mainActivityViewModel.searchPublication.observe(viewLifecycleOwner, Observer { resource ->
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

    private fun observeCategory(){
        mainActivityViewModel.categoryResponse.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> Log.i("loading", "publications by genre is loading")
                is Resource.Error -> Log.e("error", resource.message.toString())
                is Resource.Success ->{
                    if(genreChipGroup.size == 0){
                        resource.data?.let {
                            it.forEach { category ->
                                createChip(category.name)
                            }
                        }
                    }
                }
            }
        })
    }

}