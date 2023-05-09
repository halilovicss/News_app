package com.appvision.newsapp.presentation


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.R
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemsTopHeadlinesBinding
import com.appvision.newsapp.presentation.home_fragment.HomeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TopHeadlinesAdapter(
    private val homeCallback: HomeCallback
) : RecyclerView.Adapter<ViewHolder>() {
    private var itemsList = listOf<ArticleModel>()
    lateinit var binding: ItemsTopHeadlinesBinding


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        binding =
            ItemsTopHeadlinesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, homeCallback, itemsList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ArticleModel = itemsList[position]
        holder.bind(model)

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(model: List<ArticleModel>) {
        this.itemsList = model
        notifyDataSetChanged()
    }
}

class ViewHolder(
    val binding: ItemsTopHeadlinesBinding,
    homeCallback: HomeCallback,
    model: List<ArticleModel>,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: ArticleModel) {
        binding.headlineData = model
    }

    init {
        itemView.setOnClickListener {
            homeCallback.onClick(model[adapterPosition].id_key)
        }
        binding.imgSaveBookmarks.setOnClickListener {
            homeCallback.onImageClick(model[adapterPosition].id_key, 1, adapterPosition)
            binding.imgDeleteBookmarks.visibility = VISIBLE
            binding.imgSaveBookmarks.visibility = GONE
        }
        binding.imgDeleteBookmarks.setOnClickListener {
            homeCallback.onImageClick(model[adapterPosition].id_key, 0, adapterPosition)
            binding.imgSaveBookmarks.visibility = VISIBLE
            binding.imgDeleteBookmarks.visibility = GONE
        }
    }
}

@BindingAdapter("bind:loadTopImage")
fun setImageUrl(view: ImageView, urlToImage: String?) {
    urlToImage.let {
        val imgUri = it?.toUri()?.buildUpon()?.scheme("https")?.build()
        Glide.with(view.context).load(imgUri).apply(
            RequestOptions().placeholder(R.drawable.ic_loading)
        ).centerCrop().into(view)
    }
}


