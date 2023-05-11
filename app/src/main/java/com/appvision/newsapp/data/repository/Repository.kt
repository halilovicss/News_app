package com.appvision.newsapp.data.repository


import androidx.lifecycle.LiveData
import com.appvision.newsapp.BuildConfig
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.DAO
import com.appvision.newsapp.data.model.ArticleModel

class Repository(private val webService: WebService, private val dao: DAO) {
    suspend fun getTopHeadlines() = webService.getTopHeadlinesAsync("us", BuildConfig.API_KEY)
    suspend fun getAllArticles(title: String) =
        webService.getAllArticlesAsync(title, BuildConfig.API_KEY)

    fun insertBookmarkList(articleModel: ArticleModel) = dao.insertBookmarkList(articleModel)
    fun loadBookmarksList(top: Int, category: String): LiveData<List<ArticleModel>> =
        dao.loadBookmarksList(top, category)

    fun loadBookmarkArticle(id: String): ArticleModel = dao.loadBookmarkArticle(id)
    fun setFavourite(status: Int, id: String) = dao.setFavorite(status, id)
    fun loadFavouriteArticles(id: Int) = dao.loadFavouriteArticles(id)
    fun deleteFromBookmarks(id: Int) = dao.deleteFromBookmarks(id)
    fun deleteForSearch() = dao.deleteForSearch()
}