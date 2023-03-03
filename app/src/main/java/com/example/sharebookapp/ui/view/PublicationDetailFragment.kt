package com.example.sharebookapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PublicationDetailFragment : Fragment() {
    private val args: PublicationDetailFragmentArgs by navArgs()
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val publicationImage: ImageView = view.findViewById(R.id.imageDetailPublication)
        val publicationName: TextView = view.findViewById(R.id.nameDetailPublication)
        val publicationCategory: TextView = view.findViewById(R.id.categoryDetailPublication)
        val publicationDescription: TextView = view.findViewById(R.id.descriptionDetailPublication)
        val publicationCity: TextView = view.findViewById(R.id.cityDetailPublication)
        val publication = args.choosenPublication
        val addToFavorite: FloatingActionButton = view.findViewById(R.id.addToFavorite)

        publicationName.text = publication.name
        publicationCategory.text = publication.category.name
        publicationDescription.text = publication.description
        publicationCity.text = publication.city.name

        Glide.with(requireContext())
            .load(R.drawable.great)
            .into(publicationImage)

        addToFavorite.setOnClickListener {
            mainActivityViewModel.addToFavorite(publication.id)
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
    }

}