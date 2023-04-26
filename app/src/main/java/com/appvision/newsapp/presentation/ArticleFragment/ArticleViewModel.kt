package com.appvision.newsapp.presentation.ArticleFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.appvision.newsapp.data.local.LocalDatabase
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.repository.Repository
import com.appvision.newsapp.data.internet.WebService

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    var articleItem: LiveData<List<ArticleModel>>? = null

    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        repository = Repository(WebService.getInstance(), dao)
   }

    fun loadArticle(id:String){
        articleItem = repository.loadBookmarkArticle(id)
    }

    fun setFavourite(status:Int,id:String) = repository.setFavourite(status,id)
}





