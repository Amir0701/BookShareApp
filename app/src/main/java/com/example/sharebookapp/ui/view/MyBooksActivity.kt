package com.example.sharebookapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sharebookapp.App
import com.example.sharebookapp.R
import com.example.sharebookapp.data.repository.CategoryRepository
import com.example.sharebookapp.data.repository.CityRepository
import com.example.sharebookapp.data.repository.PublicationRepository
import com.example.sharebookapp.ui.model.MyBooksViewModel
import com.example.sharebookapp.ui.model.MyBooksViewModelFactory
import javax.inject.Inject

class MyBooksActivity : AppCompatActivity() {
    lateinit var myBooksViewModel: MyBooksViewModel
    @Inject
    lateinit var publicationRepository: PublicationRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository
    @Inject
    lateinit var cityRepository: CityRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_books)
        (application as App).appComponent.getMyBooksActivityComponent().inject(this)
        val viewModelFactory = MyBooksViewModelFactory((application as App), publicationRepository, categoryRepository, cityRepository)
        myBooksViewModel = ViewModelProvider(this, viewModelFactory)[MyBooksViewModel::class.java]
    }
}