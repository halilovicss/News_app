package com.appvision.newsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bookmarks")
data class ArticleModel(
    val id_key: String = UUID.randomUUID().toString(),
    val title: String?,
    val author: String?,
    val content: String?,
    val description: String?,
    @PrimaryKey val publishedAt: String,
    val url: String?,
    val urlToImage: String?,
    val name: String?,
    val category: String,
    val isFavorite: Int?,
    val top: Int?,
)


