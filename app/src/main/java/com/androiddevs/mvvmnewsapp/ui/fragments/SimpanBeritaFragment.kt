package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.BeritaAdapter
import com.androiddevs.mvvmnewsapp.ui.BeritaActivity
import com.androiddevs.mvvmnewsapp.ui.BeritaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_simpan_berita.*

class SimpanBeritaFragment : Fragment(R.layout.fragment_simpan_berita) {

    lateinit var viewModel: BeritaViewModel
    lateinit var beritaAdapter: BeritaAdapter
    val TAG = "SimpanBeritaFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as BeritaActivity).viewModel

        setupRecyclerView()

        beritaAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("artikel", it)
            }

            findNavController().navigate(
                R.id.action_simpanBeritaFragment_to_artikelFragment, bundle
            )
        }


        //membuat fungsi touch saat list di swipe ke kiri kanan
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val artikel = beritaAdapter.differ.currentList[position]
                viewModel.hapusArtikel(artikel)
                Snackbar.make(view, "Berhasil hapus artikel", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.simpanArtikel(artikel)
                    }
                    show()
                }
            }
        }

        //panggil fungsi itemTouchHelperCallback di ItemTouchHelper
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }



        //memanggil fungsi getSimpanBeritaDb untuk melihat data yang tersimpan di DB Room
        viewModel.getSimpanBeritaDb().observe(viewLifecycleOwner, Observer { artikel ->
            beritaAdapter.differ.submitList(artikel)
        })
    }

    private fun setupRecyclerView(){
        beritaAdapter = BeritaAdapter()
        rvSavedNews.apply {
            adapter = beritaAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}