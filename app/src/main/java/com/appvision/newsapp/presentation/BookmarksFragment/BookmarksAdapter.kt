package com.appvision.newsapp.presentation.BookmarksFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemArticlesBinding
import com.bumptech.glide.Glide

class BookmarksAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var bookmarksList = listOf<ArticleModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ArticleModel = bookmarksList[position]
        holder.binding.textItemArticleTitle.text = model.title
        holder.binding.textItemArticleCat.text = model.name
        Glide.with(holder.itemView).load(model.urlToImage).centerCrop()
            .into(holder.binding.imgArticle)
        holder.itemView.setOnClickListener {
            val directions =
                BookmarksFragmentDirections.actionBookmarksFragmentToArticleFragment(model.id_key.toString())
            holder.itemView.findNavController().navigate(directions)
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

class ViewHolder(val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root)
