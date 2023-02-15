package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.sharebookapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BooksFragment : Fragment() {
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
        val addPublication = view.findViewById<FloatingActionButton>(R.id.addPublicationButton)
        addPublication.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_newPublicationFragment)
        }
    }
}