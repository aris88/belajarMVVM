package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.BeritaAdapter
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
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
            //menyiapkan paket bundle data yang akan dikirimkan ke fragmen artikel
            val bundle = Bundle().apply {
                putSerializable("artikel", it)
            }

            //berpindah ke fragment artikel dan mengirimkan data melau args (bundle)
            findNavController().navigate(
                R.id.action_beritaBaruFragment_to_artikelFragment, bundle
            )
        }

        viewModel.beritaBaru.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { responseBerita ->
//                        beritaAdapter.differ.submitList(responseBerita.articles)
                        beritaAdapter.differ.submitList(responseBerita.articles.toList())
                        //mengambil total pages dan di tambah 2 karena tidak tahu total pages nya
                        val totalPages = responseBerita.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.beritaBaruPage == totalPages
                        if (isLastPage){
                            rvBeritaBaru.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Ada error: $message", Toast.LENGTH_LONG).show()
//                        Log.e(TAG, "ada error berikut: $message")
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
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoLoadingDanNoHalamanTerakhir = !isLoading && !isLastPage
            val isItemTerakhir = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNoAwal = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoLoadingDanNoHalamanTerakhir && isItemTerakhir &&
                    isNoAwal && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBeritaBaru("id")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView(){
        beritaAdapter = BeritaAdapter()
        rvBeritaBaru.apply {
            adapter = beritaAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BeritaBaruFragment.scrollListener)

        }

    }

}