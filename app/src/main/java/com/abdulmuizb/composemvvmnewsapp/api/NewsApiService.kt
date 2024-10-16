package com.abdulmuizb.composemvvmnewsapp.api

import com.abdulmuizb.composemvvmnewsapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "3467480fc0ef4e568fa9934207a830c7",
    ): NewsResponse
}