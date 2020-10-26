package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.BeritaAdapter
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_berita_baru.*

class BeritaBaruFragment : Fragment(R.layout.fragment_berita_baru) {

    lateinit var viewModel: BeritaViewModel
    lateinit var beritaAdapter: BeritaAdapter

    val TAG = "BeritaBaruFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as BeritaActivity).viewModel

        setupRecyclerView()

        beritaAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("artikel", it)
            }

            findNavController().navigate(
                R.id.action_beritaBaruFragment_to_artikelFragment, bundle
            )
        }

        viewModel.beritaBaru.observe(viewLifecycleOwner, Observer { response ->
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
                        Log.e(TAG, "ada error berikut: $message")
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
        rvBeritaBaru.apply {
            adapter = beritaAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

}