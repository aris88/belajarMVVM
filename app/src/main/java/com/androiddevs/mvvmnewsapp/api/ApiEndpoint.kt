package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.models.ResponseBerita
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("v2/top-headlines")
    suspend fun getBeritaTerbaru(
        @Query("country")
        countryCode: String = "id",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<ResponseBerita>

    @GET("v2/everything")
    suspend fun cariBerita(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<ResponseBerita>
}