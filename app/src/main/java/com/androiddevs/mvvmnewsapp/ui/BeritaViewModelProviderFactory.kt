package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.BeritaRepository

class BeritaViewModelProviderFactory (val beritaRepository: BeritaRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BeritaViewModel(beritaRepository) as T
    }

}