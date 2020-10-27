package com.androiddevs.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Artikel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class BeritaAdapter : RecyclerView.Adapter<BeritaAdapter.ArtikelViewHolder>(){

    inner class ArtikelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differArtikelCallback = object : DiffUtil.ItemCallback<Artikel>(){
        override fun areItemsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {

            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differArtikelCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
            return ArtikelViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview,parent,false))
    }

    private var onItemClickListener: ((Artikel) -> Unit)? = null

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val artikel = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(artikel.urlToImage).into(ivArticleImage)
            tvSource.text = artikel.source?.name
            tvTitle.text = artikel.title
            tvDescription.text = artikel.description
            tvPublishedAt.text = artikel.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(artikel) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (Artikel) -> Unit) {
        onItemClickListener = listener
    }
}