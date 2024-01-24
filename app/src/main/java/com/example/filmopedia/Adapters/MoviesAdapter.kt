package com.example.filmopedia.Adapters

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmopedia.Fragments.Wishlist
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.Result
import com.example.filmopedia.interfaces.MovieClickListener
import com.example.filmopedia.wishlistData.MovieDetails
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


lateinit var movieId: String
class MoviesAdapter(
    val context: Context, val popMoviesList: List<Result>, var wishlist: ArrayList<MovieDetails>
//                    val onMovieClicked: () -> Unit
)
    : RecyclerView.Adapter<MoviesAdapter.movieViewHolder>(){

    private lateinit var movieListener : MovieClickListener
    //    private lateinit var movieId: String
    private val mcontext:Context
    private var db = FirebaseDatabase.getInstance()
    private var dbRef = db.getReference("Favourites")
    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.currentUser?.uid.toString()
    private val BTN_CHECKED = "btnChecked"
    private var itemStateArray = SparseBooleanArray()

    init {
        this.mcontext=context
    }
    fun setOnMovieClickListener(listener : MovieClickListener){
        movieListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieViewHolder {
        val itemView = LayoutInflater.from(mcontext).inflate(R.layout.each_movies_item, parent, false)
        return movieViewHolder(itemView, movieListener)
    }


    override fun onBindViewHolder(holder: movieViewHolder, position: Int) {
        try {
            val currentItem = popMoviesList[position]
            Log.i("delCheck", wishlist.size.toString())
            var isWishList:Boolean = false

            for(item in wishlist)
            {
                if(item.id == currentItem.id)
                {
                    Log.i("checker",item.id.toString())
                    isWishList = true
                    break
                }
            }
            holder.wishBtn.isChecked = isWishList
            holder.movieTitle.text = currentItem.titleText.text
            holder.movieID.text = currentItem.id
            movieId = currentItem.id
            val movieName = currentItem.titleText.text
            val moviePoster = currentItem.primaryImage.url
            val releaseYear = currentItem.releaseYear.year

            holder.wishBtn.setOnClickListener {

                val randomId = popMoviesList[position].id
                if(holder.wishBtn.isChecked){
//                    holder.wishBtn.isChecked=true


                    val movie = MovieDetails(randomId, moviePoster, movieName, releaseYear)
                    wishlist.add(movie)
                    dbRef.child(uid).child(randomId).setValue(movie).addOnSuccessListener {
                        Toast.makeText(mcontext, "Added to Wishlist", Toast.LENGTH_SHORT).show()
                        holder.wishBtn.isChecked=true
                    }
                } else{
                    for((index,item) in wishlist.withIndex()){
                        if(item.id==randomId){
                            wishlist.removeAt(index)
                            break
                        }
                    }
                    dbRef.child(uid).child(randomId).removeValue().addOnSuccessListener {
                        Toast.makeText(mcontext, "Removed from Wishlist $randomId", Toast.LENGTH_SHORT).show()
                    }
                }


            }


            val url = currentItem.primaryImage.url
            Glide.with(mcontext)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.moviePoster)

        } catch (e: Exception){
            {}
        }
    }

    override fun getItemCount(): Int {
        return popMoviesList.size
    }



    class movieViewHolder(val itemView : View
                          , listener: MovieClickListener
    )
        : RecyclerView.ViewHolder(itemView) {

        var movieTitle : TextView
        var moviePoster : ShapeableImageView
        var wishBtn : CheckBox
        var movieID : TextView

        init {
            movieTitle = itemView.findViewById(R.id.movieTitle)
            moviePoster = itemView.findViewById(R.id.moviePoster)
            movieID = itemView.findViewById(R.id.movieID)
            wishBtn = itemView.findViewById(R.id.wishlist_btn)

            itemView.setOnClickListener {
                listener.onMovieClicked(adapterPosition)
            }
        }
    }
}