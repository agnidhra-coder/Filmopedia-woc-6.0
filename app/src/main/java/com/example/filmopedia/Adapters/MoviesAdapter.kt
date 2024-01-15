package com.example.filmopedia.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.Result
import com.example.filmopedia.interfaces.ItemClickListener
import com.example.filmopedia.wishlistData.MovieDetails
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var movieId: String
class MoviesAdapter(val context : Context, val popMoviesList : List<Result>) :
RecyclerView.Adapter<MoviesAdapter.movieViewHolder>(){

    private lateinit var movieListener : ItemClickListener
    private var db = FirebaseDatabase.getInstance()
    private var dbRef = db.getReference("Favourites")
    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.currentUser?.uid.toString()

    fun setOnItemClickListener(listener : ItemClickListener){
        movieListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_movies_item, parent, false)
        return movieViewHolder(itemView, movieListener)
    }


    override fun onBindViewHolder(holder: movieViewHolder, position: Int) {
        try {
            val currentItem = popMoviesList[position]
            holder.movieTitle.text = currentItem.titleText.text

            movieId = currentItem.id
            val movieName = currentItem.titleText.text
            val moviePoster = currentItem.primaryImage.url
            val releaseYear = currentItem.releaseYear.year
            holder.wishBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    val movie = MovieDetails(movieId, moviePoster, movieName, releaseYear)
                    dbRef.child(uid).child(movieId).setValue(movie).addOnSuccessListener {
                        Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
                    }

                } else{
                    dbRef.child(uid).child(movieId).removeValue().addOnSuccessListener {
                        Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }

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

    class movieViewHolder(val itemView : View, listener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var movieTitle : TextView
        var moviePoster : ShapeableImageView
        var wishBtn : CheckBox

        init {
            movieTitle = itemView.findViewById(R.id.movieTitle)
            moviePoster = itemView.findViewById(R.id.moviePoster)
            wishBtn = itemView.findViewById(R.id.wishlist_btn)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}