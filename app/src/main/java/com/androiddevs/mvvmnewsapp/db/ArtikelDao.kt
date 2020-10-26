package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Artikel

@Dao
interface ArtikelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(artikel: Artikel): Long

    @Query("SELECT * FROM artikels")
    fun getAllArtikels(): LiveData<List<Artikel>>

    @Delete
    suspend fun deleteArtikel(artikel: Artikel)
}