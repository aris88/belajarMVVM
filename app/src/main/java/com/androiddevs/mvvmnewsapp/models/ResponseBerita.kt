package com.androiddevs.mvvmnewsapp.models

data class ResponseBerita(
    val articles: MutableList<Artikel>,
    val status: String,
    val totalResults: Int
)