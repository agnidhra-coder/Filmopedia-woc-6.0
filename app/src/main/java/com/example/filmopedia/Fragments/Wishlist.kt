package com.example.filmopedia.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.Adapters.MoviesAdapter
import com.example.filmopedia.Adapters.WishlistAdapter
import com.example.filmopedia.Adapters.movieId
import com.example.filmopedia.R
import com.example.filmopedia.SwipeGesture
import com.example.filmopedia.wishlistData.MovieDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

@Suppress("DEPRECATION")
class Wishlist : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var dbRef = FirebaseDatabase.getInstance().getReference("Favourites")
    private var auth = FirebaseAuth.getInstance()
    private lateinit var adapter : WishlistAdapter
    private var uid = auth.currentUser?.uid.toString()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.wishlistRV)
        val mlist = mutableListOf<MovieDetails>()
        adapter = WishlistAdapter(requireContext(), mlist)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        dbRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(movieSnapshot in snapshot.children){
                        val movies = movieSnapshot.getValue(MovieDetails::class.java)
                        if(movies != null){
                            if(!mlist.contains(movies)){
                                mlist.add(movies)
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })


        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        adapter.deleteItem(viewHolder.adapterPosition)
                        dbRef.child(uid).child(movieId).removeValue().addOnSuccessListener {
                            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ItemTouchHelper.RIGHT -> {
                        adapter.deleteItem(viewHolder.adapterPosition)
                        dbRef.child(uid).child(movieId).removeValue().addOnSuccessListener {
                            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(recyclerView)

    }


}