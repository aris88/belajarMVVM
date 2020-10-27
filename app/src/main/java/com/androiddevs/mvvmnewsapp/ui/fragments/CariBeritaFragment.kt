package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.BeritaAdapter
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.TIME_DELAY_CARI_BERITA
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_cari_berita.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CariBeritaFragment : Fragment(R.layout.fragment_cari_berita) {

    lateinit var viewModel: BeritaViewModel
    lateinit var beritaAdapter: BeritaAdapter
    val TAG = "CariBeritaFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as BeritaActivity).viewModel

        setupRecyclerView()

        beritaAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("artikel", it)
            }

            findNavController().navigate(
                R.id.action_cariBeritaFragment_to_artikelFragment, bundle
            )
        }

        //membuat delay saat melakukan pencarian
        var job: Job? = null
        etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY_CARI_BERITA)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.cariBerita(editable.toString())
                    }
                }
            }
        }

        viewModel.cariBerita.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { responseBerita ->
                        beritaAdapter.differ.submitList(responseBerita.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Ada error: $message", Toast.LENGTH_LONG).show()
//                        Log.e(TAG, "ada error berikut: ${message}")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        beritaAdapter = BeritaAdapter()
        rvSearchNews.apply {
            adapter = beritaAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

}