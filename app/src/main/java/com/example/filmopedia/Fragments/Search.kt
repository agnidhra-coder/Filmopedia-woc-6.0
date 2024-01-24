package com.example.filmopedia.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.About
import com.example.filmopedia.Adapters.MoviesAdapter
import com.example.filmopedia.Login
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.RequestManager
import com.example.filmopedia.SearchListener
import com.example.filmopedia.interfaces.MovieClickListener
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class Search : Fragment() {
    private lateinit var searchMovie: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RequestManager
    private lateinit var progress: ProgressDialog
    private lateinit var imgButton: ImageButton
    private lateinit var SHARED_PREFS : String

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

        SHARED_PREFS = "sharedPrefs"

        imgButton = view.findViewById(R.id.moreOptions)
        imgButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showPopupMenu(v)
            }
        })

        searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progress.setTitle("Searching...")
                progress.show()
                manager.searchMovies(listener, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if(newText != null){
                            manager.searchMovies(listener, newText)
                        }
                    }
                }, 2000)
                return true
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
//            Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show()
            {}
        }else {
            val adapter = MoviesAdapter(requireContext(), response.results, arrayListOf())
            recyclerView.adapter = adapter
            adapter.setOnMovieClickListener(object: MovieClickListener {
                override fun onMovieClicked(position: Int) {
                    val bottomSheetFragment = BottomSheet()
                    bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
                    val bundle = Bundle()
                    bundle.putString("title", response.results[position].titleText.text)
                    bundle.putString("year", response.results[position].releaseYear.year.toString())
                    bundle.putString("poster", response.results[position].primaryImage.url)
                    parentFragmentManager.setFragmentResult("dataFromAdapter", bundle)
                }
            })
        }

    }

    private fun showPopupMenu(view: View?){
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return onPopupMenuClick(item)
            }
        })

        popupMenu.show()
    }
    private fun onPopupMenuClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.signOut -> signOut()
            R.id.about -> about()
        }

        return true
    }

    private fun about() {
        val intent = Intent(context, About::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(context, Login::class.java)
        startActivity(intent)
    }

}