package com.appvision.newsapp.data.repository


import androidx.lifecycle.LiveData
import com.appvision.newsapp.BuildConfig
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.DAO
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.model.NewsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val webService: WebService, private val dao: DAO) {
    fun getTop(): Flow<NewsModel> = flow {
        emit(webService.getTopHeadlinesAsync("us", BuildConfig.API_KEY))
    }

    fun getAll(title: String): Flow<NewsModel> = flow {
        emit(webService.getAllArticlesAsync(title, BuildConfig.API_KEY))
    }

    fun insertBookmarkList(articleModel: ArticleModel) = dao.insertBookmarkList(articleModel)
    fun loadBookmarksList(top: Int, category: String): LiveData<List<ArticleModel>> =
        dao.loadBookmarksList(top, category)

    fun loadBookmarkArticle(id: String): ArticleModel = dao.loadBookmarkArticle(id)
    fun setFavourite(status: Int, id: String) = dao.setFavorite(status, id)
    fun loadFavouriteArticles(id: Int) = dao.loadFavouriteArticles(id)
    fun deleteForSearch() = dao.deleteForSearch()
}