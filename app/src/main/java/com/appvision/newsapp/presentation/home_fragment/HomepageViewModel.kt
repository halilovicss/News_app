package com.appvision.newsapp.presentation.home_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.LocalDatabase
import com.appvision.newsapp.data.model.Article
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {

    val data: LocalDatabase
    var allArticleList: LiveData<List<ArticleModel>>? = null
    var topHeadlineList: LiveData<List<ArticleModel>>? = null
    private val repository: Repository

    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        data = LocalDatabase.getDatabaseData(application)
        repository = Repository(WebService.getInstance(), dao)
        viewModelScope.launch(Dispatchers.IO) {
            delete()
            loadAndSave("Facebook")
            saveHeadLine()
            delay(1000)
        }
        loadAllArticle("Facebook")
        loadTopList()
    }

    suspend fun loadAndSave(category: String) {
        val response = repository.getAllArticles(category)
        if (response.isSuccessful) {
            response.body()?.articles?.forEach {
                try {
                    repository.insertBookmarkList(mapToArticle(it, category))
                } catch (e: Exception) {
                    Log.e("TAG", "loadAndSave: ${e.message}")
                }
            }
        }
    }

    fun deleteForSearch() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteForSearch()
    }

    fun loadAllArticle(title: String) {
        allArticleList = repository.loadBookmarksList(0, title)
    }

    fun saveArticle(id: String) {
        repository.setFavourite(1, id)
    }

    fun deleteArticle(id: String) {
        repository.setFavourite(0, id)
    }

    private fun loadTopList() {
        topHeadlineList = repository.loadBookmarksList(1, "top")
    }

    private fun delete() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFromBookmarks(0)
    }

    private suspend fun saveHeadLine() {
        val response = repository.getTopHeadlines()
        response.body()?.articles?.forEach { model ->
            repository.insertBookmarkList(mapToHeadline(model))
        }
    }

    private fun mapToArticle(response: Article, category: String): ArticleModel {

        return ArticleModel(
            content = response.content,
            name = response.source?.name,
            title = response.title,
            author = response.author,
            description = response.description,
            publishedAt = response.publishedAt.toString(),
            url = response.url,
            urlToImage = response.urlToImage,
            category = category,
            isFavorite = 0,
            top = 0
        )
    }

    private fun mapToHeadline(response: Article): ArticleModel {
        return ArticleModel(
            content = response.content,
            name = response.source?.name,
            title = response.title,
            author = response.author,
            description = response.description,
            publishedAt = response.publishedAt.toString(),
            url = response.url,
            urlToImage = response.urlToImage,
            category = "top",
            isFavorite = 0,
            top = 1
        )
    }
}