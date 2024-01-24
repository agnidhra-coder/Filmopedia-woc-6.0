package com.example.filmopedia.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.example.filmopedia.SwipeGesture
import com.example.filmopedia.wishlistData.MovieDetails
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WishlistAdapter(val context: Context, val moviesList : MutableList<MovieDetails>)
    : RecyclerView.Adapter<WishlistAdapter.wishlistViewholder>(){
    private var dbRef = FirebaseDatabase.getInstance().getReference("Favourites")
    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.currentUser?.uid.toString()

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

           /* holder.fullview.setOnClickListener {
                val randomId = moviesList[position].id
                Log.i("runCheck",randomId.toString())
                //deleteItem(holder.adapterPosition)
                dbRef.child(uid).child(randomId!!).removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Removed from Wishlist $randomId", Toast.LENGTH_SHORT)
                        .show()
                }
            }*/

            val swipeGesture = object : SwipeGesture(context){

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val randomId = moviesList[position].id
                    Log.i("runCheck",randomId.toString())
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            deleteItem(viewHolder.adapterPosition)
                            dbRef.child(uid).child(randomId!!).removeValue().addOnSuccessListener {
                                Toast.makeText(context, "Removed from Wishlist $randomId", Toast.LENGTH_SHORT).show()
                            }
                        }
                        ItemTouchHelper.RIGHT -> {
                            deleteItem(viewHolder.adapterPosition)
                            dbRef.child(uid).child(randomId!!).removeValue().addOnSuccessListener {
                                Toast.makeText(context, "Removed from Wishlist $randomId", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            }

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
        var fullview: CardView

        init {
            movieTitle = itemView.findViewById(R.id.wishlistMovieTitle)
            moviePoster = itemView.findViewById(R.id.wishlistMoviePoster)
            releaseYear = itemView.findViewById(R.id.releaseYear)
            fullview = itemView.findViewById(R.id.fullview)
        }
    }

    fun deleteItem(i : Int){
        moviesList.removeAt(i)
        notifyDataSetChanged()
    }
}