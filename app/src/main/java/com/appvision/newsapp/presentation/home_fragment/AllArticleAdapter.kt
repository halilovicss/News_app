package com.appvision.newsapp.presentation.home_fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.R
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemArticlesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class AllArticleAdapter(
    private val homeCallback: HomeCallback
) : RecyclerView.Adapter<ViewHolders>() {
    private var allArticles = listOf<ArticleModel>()
    private lateinit var binding: ItemArticlesBinding

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolders {
        binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding, homeCallback)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        try {
            val model: ArticleModel = allArticles[position]
            holder.bind(model)
        } catch (_: Exception) {

        }
    }

    override fun getItemCount(): Int {
        return allArticles.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ArticleModel>) {
        if (this.allArticles != list) {
            this.allArticles = list
            notifyDataSetChanged()
        }
    }
}

class ViewHolders(
    val binding: ItemArticlesBinding,
    homeCallback: HomeCallback,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: ArticleModel) {
        binding.itemData = model
    }

    init {
        itemView.setOnClickListener {
            binding.itemData?.id_key?.let { id -> homeCallback.onClick(id) }
        }
    }
}

@BindingAdapter("bind:loadImage")
fun setImageUrl(view: ImageView, urlToImage: String?) {
    view.outlineProvider = ViewOutlineProvider.BACKGROUND
    view.clipToOutline = true
    Glide.with(view.context).load(urlToImage).transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions().placeholder(R.drawable.ic_loading)).into(view)
}