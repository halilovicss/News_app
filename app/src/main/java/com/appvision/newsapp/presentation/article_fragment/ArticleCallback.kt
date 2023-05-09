package com.appvision.newsapp.presentation.article_fragment

interface ArticleCallback {
    fun shareArticle(articleUrl: String)

    fun openArticle(articleUrl: String)

    fun createToolbarMenu(
        status: Int?, title: String, shareUrl: String, toggleBookmark: (Boolean) -> Unit
    )

}