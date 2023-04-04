package com.example.sharebookapp.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharebookapp.R
import com.example.sharebookapp.data.model.Category
import javax.inject.Inject

class CategoryAdapter @Inject constructor(): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var categoryList: List<Category> = ArrayList()

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        var isSelected = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item_recycler, parent, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.name

        holder.categoryName.setOnClickListener {
            if(holder.isSelected){
                it.background = it.context.getDrawable(R.drawable.category_item_background)
                holder.isSelected = false
            }
            else{
                it.background = it.context.getDrawable(R.drawable.category_item_selected)
                holder.isSelected = true
            }

            onGenreSelectedItem?.onGenreItemClick(category, holder.isSelected)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface OnGenreSelectedItem{
        fun onGenreItemClick(category: Category, isSelected: Boolean)
    }

    private var onGenreSelectedItem: OnGenreSelectedItem? = null

    fun setOnGenreClickListener(onGenreSelectedItem: OnGenreSelectedItem){
        this.onGenreSelectedItem = onGenreSelectedItem
    }
}