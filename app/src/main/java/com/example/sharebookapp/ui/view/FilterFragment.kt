package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharebookapp.R
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource

class FilterFragment : Fragment() {
    private lateinit var citySpinner: Spinner
    private lateinit var confirmButton: TextView
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observe()
        mainActivityViewModel.getAllCities()
    }

    private fun initView(view: View){
        citySpinner = view.findViewById(R.id.cityFilterList)
        confirmButton = view.findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cityName", citySpinner.selectedItem as String)
            findNavController().navigate(R.id.action_filterFragment_to_booksFragment, bundle)
        }
    }

    private fun observe(){
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
                        citySpinner.adapter = spinnerAdapter
                    }
                }

                is Resource.Loading -> {
                    Log.i("loading", "Cities is loading")
                }

                is Resource.Error -> {
                    Log.e("observeCity", resource.message.toString())
                }
            }
        })
    }
}