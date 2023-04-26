package com.appvision.newsapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.R
import com.appvision.newsapp.databinding.ItemArticlesBinding
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.utils.OnClickListener
import com.bumptech.glide.Glide


class AllArticleAdapter(private val onItemClickListener: OnClickListener) :
    RecyclerView.Adapter<ViewHolders>() {
    private var allArticles = listOf<ArticleModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolders {
        val view = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(view, onItemClickListener, allArticles)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        try {
            val model: ArticleModel = allArticles[position]
            val imgUrl = model.urlToImage
            holder.binding.textItemArticleCat.text = model.name
            holder.binding.textItemArticleTitle.text = model.title
            if (imgUrl.isNullOrEmpty()) {
                holder.binding.imgArticle.setImageResource(R.drawable.ic_categories)
                return
            }
            Glide.with(holder.itemView).load(model.urlToImage).centerCrop()
                .into(holder.binding.imgArticle)
        } catch (e: Exception) {
            Log.e("TAG", "onBindViewHolder: $e")
        }
    }

    override fun getItemCount(): Int {
        return 30
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ArticleModel>) {
        this.allArticles = list
        notifyDataSetChanged()
    }

}

class ViewHolders(
    val binding: ItemArticlesBinding,
    onItemClickListener: OnClickListener,
    model: List<ArticleModel>
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onItemClickListener.onClick(adapterPosition, model = model)
        }
    }
}
