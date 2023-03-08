package com.example.sharebookapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Profile
import javax.inject.Inject

class ProfileAdapter @Inject constructor(): RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){
    private var profileList: List<Profile> = listOf()

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val icon: ImageView = itemView.findViewById(R.id.profileItemImage)
        val title: TextView = itemView.findViewById(R.id.profileItemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profileList[position]
        holder.icon.setImageResource(profile.imageResource)
        holder.title.text = profile.title
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    fun setProfile(list: List<Profile>){
        profileList = list
    }
}