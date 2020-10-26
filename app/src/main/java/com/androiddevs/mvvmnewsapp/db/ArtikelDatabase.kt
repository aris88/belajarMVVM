package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Artikel

@Database(
    entities = [Artikel::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArtikelDatabase : RoomDatabase(){

    abstract fun getArtikelDao(): ArtikelDao

    companion object{
        @Volatile
        private var instance: ArtikelDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArtikelDatabase::class.java,
                "artikel_db.db"
            ).build()
    }



}