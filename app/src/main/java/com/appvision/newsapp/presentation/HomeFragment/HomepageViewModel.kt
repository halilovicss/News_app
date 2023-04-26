package com.appvision.newsapp.presentation.HomeFragment

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
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    var bookmark: LiveData<List<ArticleModel>>? = null
    var headLineList: LiveData<List<ArticleModel>>? = null
    val data: LocalDatabase

    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        data = LocalDatabase.getDatabaseData(application)
        repository = Repository(WebService.getInstance(), dao)

        repository.deleteFromBookmarks(0)
        viewModelScope.launch {
            loadAndSave("Facebook")
            saveHeadLine()
        }
        loadBookmark("Facebook")
        headLineList = repository.loadBookmarksList(1, "top")

    }

    fun loadBookmark(title: String) {
        bookmark = repository.loadBookmarksList(0, title)
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


    fun deleteForSearch(): Unit = repository.deleteForSearch()


    private suspend fun saveHeadLine() {
        val response = repository.getTopHeadlines()
        response.body()!!.articles.forEach { model ->
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




