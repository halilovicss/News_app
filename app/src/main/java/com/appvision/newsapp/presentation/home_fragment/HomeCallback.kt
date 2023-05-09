package com.appvision.newsapp.presentation.home_fragment

interface HomeCallback {
    fun onClick(id: String)

    fun onImageClick(id: String, status: Int?, position: Int)

    fun onCategoryClick(title: String)

}