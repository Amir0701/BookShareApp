package com.example.sharebookapp.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.sharebookapp.R
import com.example.sharebookapp.ui.model.MainActivityViewModel
import com.example.sharebookapp.util.Resource

class FilterDialog: DialogFragment() {
    private lateinit var citySpinner: Spinner
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
            .setTitle("Выберите город")
            .setView(R.layout.filter_dialog)
            .setPositiveButton("Ok", null)
            .setNegativeButton("cancel", null)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citySpinner = view.findViewById(R.id.filterCityList)
        viewModel = (activity as MainActivity).mainActivityViewModel
        observeCity()
        viewModel.getAllCities()
    }

    private fun observeCity(){
        viewModel.cityResponse.observe(viewLifecycleOwner, Observer {resource ->
            when(resource){
                is Resource.Loading ->{
                    Log.i("load", "city is loading")
                }
                is Resource.Error -> Log.e("error", resource.message.toString())
                is Resource.Success ->{
                    resource.data?.let { cities ->
                        val str = mutableListOf<String>()
                        var position = 0
                        var i = 0
                        cities.forEach{
                            str.add(it.name)
                        }
                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, str)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        citySpinner.adapter = spinnerAdapter

                        citySpinner.setSelection(0)
                    }
                }
            }
        })
    }
}