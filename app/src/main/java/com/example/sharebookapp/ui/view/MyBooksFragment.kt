package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.ui.model.MyBooksViewModel
import com.example.sharebookapp.util.Resource
import javax.inject.Inject

class MyBooksFragment : Fragment() {
    @Inject
    lateinit var myBookAdapter: MyBookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var myBooksViewModel: MyBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ((activity as MyBooksActivity).application as App).appComponent
            .getMyBooksActivityComponent()
            .getMyBooksFragmentComponent()
            .inject(this)

        recyclerView = view.findViewById(R.id.myBooksRecycler)
        setUpRecycler()
    }

    override fun onStart() {
        super.onStart()
        myBooksViewModel = (activity as MyBooksActivity).myBooksViewModel
        observe()
        myBooksViewModel.getPublications()
    }


    private fun setUpRecycler(){
        recyclerView.adapter = myBookAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observe(){
        myBooksViewModel.publications.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Loading -> Log.i("loading", "my publications are loading")
                is Resource.Error -> Log.i("my books", resource.message.toString())
                is Resource.Success -> {
                    resource.data?.let {
                        myBookAdapter.setPublications(it)
                    }
                }
            }
        })
    }
}