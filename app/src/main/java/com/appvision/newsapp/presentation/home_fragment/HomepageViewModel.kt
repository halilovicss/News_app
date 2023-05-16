package com.appvision.newsapp.presentation.home_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.LocalDatabase
import com.appvision.newsapp.data.model.Article
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {

    val data: LocalDatabase
    var allArticleList: LiveData<List<ArticleModel>>? = null
    var topHeadlineList: LiveData<List<ArticleModel>>? = null
    private val repository: Repository

    private var isConnected = MutableLiveData<Boolean>()
    val isConnectedView: LiveData<Boolean> get() = isConnected


    //  val jobHeadline
    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        data = LocalDatabase.getDatabaseData(application)
        repository = Repository(WebService.getInstance(), dao)
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.async(Dispatchers.IO) {
                Log.d("TAG", "Fetch top article: ${this.coroutineContext}")
                fetchTop()
            }.await()
            viewModelScope.async(Dispatchers.IO) {
                Log.d("TAG", "Feetch all article: ${this.coroutineContext}")
                fetchAll("Facebook")
            }.await()


        }
        loadAllArticle("Facebook")
        loadTopList()

    }

    private suspend fun fetchTop() {
        repository.getTop().flowOn(Dispatchers.IO)
            .catch { _ -> }
            .collect {
                it.articles.forEach {
                    repository.insertBookmarkList(mapToHeadline(it))
                }
            }
    }

    suspend fun fetchAll(title: String) {
        repository.getAll(title).flowOn(Dispatchers.IO)
            .catch { _ ->
                isConnected.postValue(false)
                Log.d("TAG", "fetchAll: Greska ${this.toString()}")
            }
            .collect { newsModel ->
                isConnected.postValue(true)
                newsModel.articles.forEach {
                    repository.insertBookmarkList(mapToArticle(it, title))
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