package com.example.filmopedia.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.Adapters.MoviesAdapter
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.Result
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.RequestManager
import com.example.filmopedia.SearchListener

@Suppress("DEPRECATION")
class Search : Fragment() {
    private lateinit var searchMovie: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RequestManager
    private lateinit var progress: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchMovie = view.findViewById(R.id.searchMovie)
        recyclerView = view.findViewById(R.id.searchRV)
        manager = RequestManager(requireContext())
        progress = ProgressDialog(requireContext())

        searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progress.setTitle("Searching...")
                progress.show()
                manager.searchMovies(listener, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private var listener = object : SearchListener{
        override fun onResponse(response: SearchData?) {
            progress.dismiss()
            showResult(response!!)
        }

        override fun onError(msg: String) {
            progress.dismiss()
            Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showResult(response: SearchData) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        if (response.results.isEmpty()){
            Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show()
        }else {
            val adapter = MoviesAdapter(requireContext(), response.results)
            recyclerView.adapter = adapter
        }

    }

}