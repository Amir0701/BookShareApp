package com.example.sharebookapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Profile
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var profileAdapter: ProfileAdapter
    private lateinit var profileRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).mainActivityComponent.getProfileFragmentComponent().inject(this)
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val list = mutableListOf<Profile>()
        list.add(Profile("Мои книги", R.drawable.my_book_30_mdpi))
        list.add(Profile("Редактировать профиль",R.drawable.ic_edit))
        list.add(Profile("Поменять пароль", R.drawable.ic_password_24))
        list.add(Profile("Информация", R.drawable.ic__info_24))
        list.add(Profile("Выход",R.drawable.ic_exit_to_app))
        profileAdapter.setProfile(list)
        profileRecyclerView.adapter = profileAdapter
        profileRecyclerView.layoutManager = LinearLayoutManager(context)
    }
}