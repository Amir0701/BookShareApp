package com.example.sharebookapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ioc.scope.MyBooksScope
import javax.inject.Inject

@MyBooksScope
class MyBookAdapter @Inject constructor() : RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
    val differ = object : DiffUtil.ItemCallback<Publication>(){
        override fun areItemsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem == newItem
        }

    }

    private val list = AsyncListDiffer(this, differ)

    class MyBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.myBookImage)
        val title: TextView = itemView.findViewById(R.id.myBookTitle)
        val author: TextView = itemView.findViewById(R.id.myBookAuthor)
        val genre: TextView = itemView.findViewById(R.id.myBookGenre)
        val date: TextView = itemView.findViewById(R.id.myBookDate)
        val description: TextView = itemView.findViewById(R.id.myBookDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_books_item, parent, false)
        return MyBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyBookViewHolder, position: Int) {
        val publication = list.currentList[position]
        holder.title.text = publication.name
        holder.genre.text = publication.category.name
        holder.date.text = publication.publishedAt.toString()
        holder.description.text = publication.description

        Glide.with(holder.itemView)
            .load(R.drawable.great)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.currentList.size
    }

    fun setPublications(publications: List<Publication>){
        list.submitList(publications)
    }
}