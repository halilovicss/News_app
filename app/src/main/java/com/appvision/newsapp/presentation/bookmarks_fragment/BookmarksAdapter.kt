package com.appvision.newsapp.presentation.bookmarks_fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.R
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemArticlesBinding

class BookmarksAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var bookmarksList = listOf<ArticleModel>()
    lateinit var binding: ItemArticlesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ArticleModel = bookmarksList[position]
        holder.bind(model)
        holder.itemView.setOnClickListener {
            val bundle = bundleOf("articleId" to model.id_key)
            holder.itemView.findNavController()
                .navigate(R.id.action_bookmarkFragment_to_articleFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return bookmarksList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ArticleModel>) {
        this.bookmarksList = list
        notifyDataSetChanged()
    }
}

class ViewHolder(val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: ArticleModel) {
        binding.itemData = model
    }
}
