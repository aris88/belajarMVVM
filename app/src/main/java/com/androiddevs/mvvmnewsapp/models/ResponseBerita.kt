package com.androiddevs.mvvmnewsapp.models

data class ResponseBerita(
    val articles: List<Artikel>,
    val status: String,
    val totalResults: Int
)