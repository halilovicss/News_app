package com.appvision.newsapp.data.internet

import com.appvision.newsapp.BuildConfig
import com.appvision.newsapp.data.model.NewsModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("top-headlines")
    suspend fun getTopHeadlinesAsync(
        @Query("country") country: String, @Query("apiKey") key: String
    ): Response<NewsModel>

    @GET("everything")
    suspend fun getAllArticlesAsync(
        @Query("q") title: String, @Query("apiKey") key: String
    ): Response<NewsModel>

    companion object {
        private var webService: WebService? = null
        fun getInstance(): WebService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            if (webService == null) {
                val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                webService = retrofit.create(WebService::class.java)
            }
            return webService!!
        }
    }
}