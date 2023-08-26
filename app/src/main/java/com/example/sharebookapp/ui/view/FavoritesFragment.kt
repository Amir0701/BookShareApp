package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource
import javax.inject.Inject

class FavoritesFragment : Fragment() {
    @Inject
    lateinit var bookAdapter: BookAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Frag", "Favorites Fragment")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).mainActivityComponent.getFavoritesFragmentComponent()
            .inject(this)
        favoriteRecycler = view.findViewById(R.id.favoriteRecycler)
        progressBar = view.findViewById(R.id.favoriteProgressBar)
        setUpRecycler()
        setUpClickListener()
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observeToFavoritePublications()
        mainActivityViewModel.getFavoritePublication()
    }

    private fun observeToFavoritePublications(){
        mainActivityViewModel.favoritePublication.observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let {
                        bookAdapter.setPublication(it)
                    }
                    progressBar.visibility = View.GONE
                }

                is Resource.Loading -> {
                    Log.i("observeFavorite", "favorite publications is loading")
                    progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    Log.e("observeFavorite", resource.message.toString())
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun setUpRecycler(){
        favoriteRecycler.adapter = bookAdapter
        favoriteRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setUpClickListener(){
        bookAdapter.setOnBookItemClickListener(object : BookAdapter.OnBookItemClickListener{
            override fun onItemClick(publication: Publication) {
                val bundle = Bundle()
                bundle.putSerializable("choosenPublication", publication)
                findNavController().navigate(R.id.action_favoritesFragment_to_publicationDetailFragment, bundle)
            }
        })
    }
}