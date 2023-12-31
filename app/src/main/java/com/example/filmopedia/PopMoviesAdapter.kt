package com.example.filmopedia

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.filmopedia.RapidAPIData.Result
import com.google.android.material.imageview.ShapeableImageView

class PopMoviesAdapter(val context : Context, val popMoviesList : List<Result>) :
RecyclerView.Adapter<PopMoviesAdapter.popViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_pop_movies_item, parent, false)
        return popViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: popViewHolder, position: Int) {
        val currentItem = popMoviesList[position]
        holder.movieTitle.text = currentItem.titleText.text

        val url = currentItem.primaryImage.url

        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .into(holder.moviePoster)
    }

    override fun getItemCount(): Int {
        return popMoviesList.size
    }

    class popViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        var movieTitle : TextView
        var moviePoster : ShapeableImageView

        init {
            movieTitle = itemView.findViewById(R.id.popMovieTitle)
            moviePoster = itemView.findViewById(R.id.popMoviePoster)
        }
    }
}