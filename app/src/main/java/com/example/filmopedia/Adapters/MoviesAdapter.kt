package com.example.filmopedia.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.Result
import com.example.filmopedia.interfaces.ItemClickListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MoviesAdapter(val context : Context, val popMoviesList : List<Result>) :
RecyclerView.Adapter<MoviesAdapter.popViewHolder>(){

    private lateinit var movieListener : ItemClickListener
    private lateinit var db : FirebaseDatabase
    private lateinit var dbRef : DatabaseReference
    fun setOnItemClickListener(listener : ItemClickListener){
        movieListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_movies_item, parent, false)
        return popViewHolder(itemView, movieListener)
    }


    override fun onBindViewHolder(holder: popViewHolder, position: Int) {
        try {
            val currentItem = popMoviesList[position]
            holder.movieTitle.text = currentItem.titleText.text

            val url = currentItem.primaryImage.url

            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster)
        } catch (e: Exception){
            Toast.makeText(context, "No movie found", Toast.LENGTH_SHORT).show()}

    }

    override fun getItemCount(): Int {
        return popMoviesList.size
    }

    class popViewHolder(val itemView : View
                        ,listener: ItemClickListener
    )
        : RecyclerView.ViewHolder(itemView) {
        var movieTitle : TextView
        var moviePoster : ShapeableImageView

        init {
            movieTitle = itemView.findViewById(R.id.movieTitle)
            moviePoster = itemView.findViewById(R.id.moviePoster)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}