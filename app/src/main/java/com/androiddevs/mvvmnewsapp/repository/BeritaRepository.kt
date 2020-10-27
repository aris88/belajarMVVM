package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.ApiService
import com.androiddevs.mvvmnewsapp.db.ArtikelDatabase
import com.androiddevs.mvvmnewsapp.models.Artikel

class BeritaRepository (val db: ArtikelDatabase){

    suspend fun getBeritaTerbaru(countryCode: String, pageNumber: Int)=
        ApiService.api.getBeritaTerbaru(countryCode, pageNumber)

    suspend fun cariBerita(searchQuery: String, pageNumber: Int) =
        ApiService.api.cariBerita(searchQuery, pageNumber)

    suspend fun simpanBerita(artikel: Artikel) = db.getArtikelDao().upsert(artikel)

    fun getSimpanBeritaDb() = db.getArtikelDao().getAllArtikels()

    suspend fun hapusArtikel(artikel: Artikel) = db.getArtikelDao().deleteArtikel(artikel)
}