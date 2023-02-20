package com.example.sharebookapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Publication
import com.example.sharebookapp.ioc.scope.MainActivityScope
import javax.inject.Inject

@MainActivityScope
class BookAdapter @Inject constructor(): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardImage: ImageView = view.findViewById(R.id.cardImageBook)
        val cardTitle: TextView = view.findViewById(R.id.cardTitleBook)
        val cardDate: TextView = view.findViewById(R.id.cardDate);
        val cardGenre: TextView = view.findViewById(R.id.cardGenreBook);
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Publication>(){
        override fun areItemsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem == newItem
        }
    }

    private val publications = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentPublication = publications.currentList[position]
        holder.cardTitle.text = currentPublication.name
        holder.cardDate.text = currentPublication.publishedAt.toString()

        holder.itemView.setOnClickListener {
            onBookItemClickListener?.onItemClick(currentPublication)
        }
    }

    override fun getItemCount(): Int {
        return publications.currentList.size
    }

    fun setPublication(publicationList: List<Publication>){
        publications.submitList(publicationList)
    }

    private var onBookItemClickListener: OnBookItemClickListener? = null

    fun setOnBookItemClickListener(onBookItemClickListener: OnBookItemClickListener){
        this.onBookItemClickListener = onBookItemClickListener
    }

    interface OnBookItemClickListener{
        fun onItemClick(publication: Publication)
    }


}