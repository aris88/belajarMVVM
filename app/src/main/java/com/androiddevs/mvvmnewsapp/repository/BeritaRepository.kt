package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.ApiService
import com.androiddevs.mvvmnewsapp.db.ArtikelDatabase

class BeritaRepository (val db: ArtikelDatabase){

    suspend fun getBeritaTerbaru(countryCode: String, pageNumber: Int) =
        ApiService.api.getBeritaTerbaru(countryCode, pageNumber)
}