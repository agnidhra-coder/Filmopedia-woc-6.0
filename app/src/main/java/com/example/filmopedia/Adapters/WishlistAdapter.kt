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
import com.example.filmopedia.wishlistData.MovieDetails
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.FirebaseDatabase

class WishlistAdapter(val context: Context, val moviesList : MutableList<MovieDetails>)
    : RecyclerView.Adapter<WishlistAdapter.wishlistViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wishlistViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_wishlist_item, parent, false)
        return wishlistViewholder(view)
    }

    override fun onBindViewHolder(holder: wishlistViewholder, position: Int) {
        try {
            val currentItem = moviesList[position]
            holder.movieTitle.text = currentItem.movieTitle
            Glide.with(context)
                .load(currentItem.img)
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster)
            holder.releaseYear.text = currentItem.releaseYear.toString()

        } catch (e: Exception){
            Toast.makeText(context, "No movie found", Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return moviesList.size
    }
    class wishlistViewholder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieTitle : TextView
        var moviePoster : ShapeableImageView
        var releaseYear : TextView

        init {
            movieTitle = itemView.findViewById(R.id.wishlistMovieTitle)
            moviePoster = itemView.findViewById(R.id.wishlistMoviePoster)
            releaseYear = itemView.findViewById(R.id.releaseYear)
        }
    }

    fun deleteItem(i : Int){
        moviesList.removeAt(i)
        notifyDataSetChanged()
    }
}