package com.appvision.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.appvision.newsapp.data.model.ArticleModel

@Dao
interface DAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBookmarkList(articleModel: ArticleModel):Long

    @Query("SELECT * FROM bookmarks WHERE top = :top AND category = :category ORDER by id_key DESC")
    fun loadBookmarksList(top:Int, category:String): LiveData<List<ArticleModel>>

    @Query("SELECT * FROM bookmarks WHERE id_key = :id")
    fun loadBookmarkArticle(id: String): LiveData<List<ArticleModel>>

    @Query("UPDATE bookmarks SET isFavorite = :status WHERE id_key = :id")
    fun setFavorite(status:Int,id:String)

    @Query("SELECT * FROM bookmarks WHERE isFavorite = :fav ORDER by id_key DESC")
    fun loadFavouriteArticles(fav:Int): LiveData<List<ArticleModel>>

    @Query("DELETE FROM bookmarks WHERE isFavorite = :id")
    fun deleteFromBookmarks(id: Int)

    @Query("DELETE FROM bookmarks WHERE isFavorite = 0 AND top = 0")
    fun deleteForSearch()

}