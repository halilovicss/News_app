package com.appvision.newsapp.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.databinding.ItemsTopHeadlinesBinding
import com.appvision.newsapp.utils.OnClickListener
import com.bumptech.glide.Glide

class TopHeadlinesAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ViewHolder>() {
    private var itemsList = listOf<ArticleModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            ItemsTopHeadlinesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClickListener, itemsList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val model: ArticleModel = itemsList[position]
            holder.binding.textItemTitle.text = model.title
            holder.binding.textItemCategories.text = model.name
            Glide.with(holder.itemView).load(model.urlToImage).centerCrop()
                .into(holder.binding.imgHeadlineItem)
            when (model.isFavorite) {
                1 -> {
                    holder.binding.imgSaveBookmarks.visibility = View.GONE
                    holder.binding.imgDeleteBookmarks.visibility = View.VISIBLE
                }
                else -> {
                    holder.binding.imgSaveBookmarks.visibility = View.VISIBLE
                    holder.binding.imgDeleteBookmarks.visibility = View.GONE
                }


            }
        } catch (e: Exception) {
            Log.e("TAG", "onBindViewHolder: ${e.message}", )
        }

    }

    override fun getItemCount(): Int {
        return 10
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(model: List<ArticleModel>) {
        this.itemsList = model
        notifyDataSetChanged()
    }


}

class ViewHolder(
    val binding: ItemsTopHeadlinesBinding,
    clickListener: OnClickListener,
    model: List<ArticleModel>,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            clickListener.onClick(adapterPosition, model = model)
        }

        binding.imgSaveBookmarks.setOnClickListener {
            clickListener.onImageClick(adapterPosition, model)
        }


    }
}
