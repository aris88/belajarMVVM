package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.db.ArtikelDatabase
import com.androiddevs.mvvmnewsapp.repository.BeritaRepository
import kotlinx.android.synthetic.main.activity_berita.*

class BeritaActivity : AppCompatActivity() {

    lateinit var viewModel: BeritaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berita)

        val beritaRepository = BeritaRepository(ArtikelDatabase(this))
        val viewModelProviderFactory = BeritaViewModelProviderFactory(beritaRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BeritaViewModel::class.java)

        bottomNavigationView.setupWithNavController(beritaNavHostFragment.findNavController())
    }
}
