package com.appvision.newsapp.presentation.article_fragment

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appvision.newsapp.data.internet.WebService
import com.appvision.newsapp.data.local.LocalDatabase
import com.appvision.newsapp.data.model.ArticleModel
import com.appvision.newsapp.data.repository.Repository
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ArticleViewModel(
    private val callback: ArticleCallback, application: Application
) : AndroidViewModel(application), Observable {
    val setFavourite: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    private val repository: Repository
    private var localArticle: ArticleModel? = null

    @get:Bindable
    var articleTitle by Delegates.observable("") { _, _, _ -> notifyPropertyChanged(BR.articleTitle) }

    @get:Bindable
    var articleAuthor by Delegates.observable("") { _, _, _ -> notifyPropertyChanged(BR.articleAuthor) }

    @get:Bindable
    var articleDescription by Delegates.observable("") { _, _, _ -> notifyPropertyChanged(BR.articleDescription) }

    @get:Bindable
    var articleCategory by Delegates.observable("") { _, _, _ -> notifyPropertyChanged(BR.articleCategory) }

    @get:Bindable
    var articleImageUrl by Delegates.observable("") { _, _, _ -> notifyPropertyChanged(BR.articleImageUrl) }


    init {
        val dao = LocalDatabase.getDatabaseData(application).getDAO()
        repository = Repository(WebService.getInstance(), dao)
    }

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                callbacks = PropertyChangeRegistry()
            }
        }
        callbacks?.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) return
        }
        callbacks?.remove(callback)
    }

    private fun handleArticleUI(articleData: ArticleModel?) {
        localArticle = articleData
        articleTitle = articleData?.title ?: ""
        articleAuthor = articleData?.author ?: ""
        articleDescription = articleData?.description ?: ""
        articleCategory = articleData?.name ?: ""
        articleImageUrl = articleData?.urlToImage ?: ""
        setFavourite.postValue(articleData?.isFavorite)
        callback.createToolbarMenu(
            setFavourite.value, articleTitle.take(13), localArticle?.url ?: "", ::toggleBookmark
        )
    }

    private fun toggleBookmark(state: Boolean) {
        when (state) {
            true -> saveArticle()
            false -> deleteArticle()
        }
    }

    private fun saveArticle() {
        localArticle?.id_key?.let { setFavourite(1, it) }
        setFavourite.postValue(1)
    }

    private fun deleteArticle() {
        localArticle?.id_key?.let { setFavourite(0, it) }
        setFavourite.postValue(0)
    }

    private fun setFavourite(status: Int, id: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.setFavourite(status, id)
    }

    private fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (callbacks == null) return
        }
        callbacks?.notifyCallbacks(this, fieldId, null)
    }

    fun openArticle() {
        localArticle?.url?.let { callback.openArticle(it) }
    }
    fun loadArticle(id: String) {
        Log.d("TAG", "loadArticle: Load")
        handleArticleUI(repository.loadBookmarkArticle(id))
    }
}

@BindingAdapter("bind:imageUrls")
fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}