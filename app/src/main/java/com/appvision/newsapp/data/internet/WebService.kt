package com.appvision.newsapp.data.internet

import com.appvision.newsapp.data.model.NewsModel
import com.appvision.newsapp.utils.Constants.API
import com.appvision.newsapp.utils.Constants.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country")country:String,@Query("apiKey")key: String): Response<NewsModel>

    @GET("everything")
    suspend fun getAllArticles(@Query("q")title:String,@Query("apiKey") key:String): Response<NewsModel>

    companion object{
        var webService: WebService? = null
        fun getInstance(): WebService {
            if (webService == null){
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                webService = retrofit.create(WebService::class.java)
            }
            return webService!!
        }
    }
}