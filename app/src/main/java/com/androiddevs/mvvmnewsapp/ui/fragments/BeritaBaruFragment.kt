package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel

class BeritaBaruFragment : Fragment(R.layout.fragment_berita_baru) {

    lateinit var viewModel: BeritaViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as BeritaActivity).viewModel
    }

}