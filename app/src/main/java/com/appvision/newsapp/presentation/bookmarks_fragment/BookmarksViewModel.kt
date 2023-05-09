package com.appvision.newsapp.presentation.bookmarks_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.LocalDatabase
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.repository.Repository

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val bookmarksList: LiveData<List<ArticleModel>>

    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        repository = Repository(WebService.getInstance(), dao)
        bookmarksList = repository.loadFavouriteArticles(1)
    }
}