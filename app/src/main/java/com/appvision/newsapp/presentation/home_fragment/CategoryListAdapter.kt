package com.appvision.newsapp.presentation.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private val list: Array<String>, private val homeCallback: HomeCallback) :
    RecyclerView.Adapter<ViewHolderC>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderC {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderC(view)
    }

    override fun onBindViewHolder(holder: ViewHolderC, position: Int) {
        holder.binding.textCategoryTitle.text = list[position]
        holder.itemView.setOnClickListener {
            homeCallback.onCategoryClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ViewHolderC(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
