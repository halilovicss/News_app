package com.appvision.newsapp.utils


import com.appvision.newsapp.data.model.ArticleModel

interface OnClickListener {
    fun onClick(position: Int,model:List<ArticleModel>)
    fun onImageClick(position: Int,model: List<ArticleModel>)
}