package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_artikel.*

class ArtikelFragment : Fragment(R.layout.fragment_artikel) {

    lateinit var viewModel: BeritaViewModel
    val args: ArtikelFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as BeritaActivity).viewModel

        //menerima data artikel yang dikirimkan dari BeritaBaruFragment
        val artikel = args.artikel

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(artikel.url)
        }

        fab.setOnClickListener {
            viewModel.simpanArtikel(artikel)
            Snackbar.make(view, "Artikel berhasil di simpan", Snackbar.LENGTH_SHORT).show()
        }

    }
}