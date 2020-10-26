package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel

class SimpanBeritaFragment : Fragment(R.layout.fragment_simpan_berita) {

    lateinit var viewModel: BeritaViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as BeritaActivity).viewModel
    }
}