package com.appvision.newsapp.presentation.home_fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.R
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemArticlesBinding
import com.bumptech.glide.Glide
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
        return ViewHolders(binding, homeCallback, allArticles)
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
        this.allArticles = list
        notifyDataSetChanged()
    }
}

class ViewHolders(
    val binding: ItemArticlesBinding,
    homeCallback: HomeCallback,
    model: List<ArticleModel>,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: ArticleModel) {
        binding.itemData = model
    }

    init {
        itemView.setOnClickListener {
            model[adapterPosition].id_key.let { it1 -> homeCallback.onClick(it1) }
        }
    }
}

@BindingAdapter("bind:loadImage")
fun setImageUrl(view: ImageView, urlToImage: String?) {
    urlToImage.let {
        val imgUri = it?.toUri()?.buildUpon()?.scheme("https")?.build()
        Glide.with(view.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_loading)
            ).into(view)
    }
}