package com.example.filmopedia.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.About
import com.example.filmopedia.interfaces.MovieApiInterface
import com.example.filmopedia.Adapters.MoviesAdapter
import com.example.filmopedia.Login
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.RapidAPIData.genres.Genres
import com.example.filmopedia.interfaces.GenreInterface
import com.example.filmopedia.interfaces.GenreListInterface
import com.example.filmopedia.interfaces.MovieClickListener
import com.example.filmopedia.wishlistData.MovieDetails
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class Home : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var popView: RecyclerView
    lateinit var filterView: RecyclerView
    lateinit var popMoviesAdapter: MoviesAdapter
    lateinit var genreDropdown: Spinner
    lateinit var imgButton : ImageButton
    private var dbRef = FirebaseDatabase.getInstance().getReference("Favourites")
    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.currentUser?.uid.toString()
    private lateinit var SHARED_PREFS : String
    private val baseUrl = "https://moviesdatabase.p.rapidapi.com/"
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SHARED_PREFS = "sharedPrefs"
        val popShimmerFrameLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerView)
        popShimmerFrameLayout.startShimmer()

        popView = view.findViewById(R.id.popMoviesRecyclerView)
        filterView = view.findViewById(R.id.filterMoviesRecyclerView)

        imgButton = view.findViewById(R.id.moreOptions)
        imgButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showPopupMenu(v)
            }
        })

        val rapidRetrofitData = retrofitBuilder
                                .create(MovieApiInterface::class.java)
                                .getPopularData()

        rapidRetrofitData.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                try{
                    if(context!=null) {
                        val responseBody = response.body()
                        val resultList = responseBody?.results!!
                        val mlist = arrayListOf<MovieDetails>()
                        dbRef.child(uid).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(context!=null){
                                    if (snapshot.exists()) {
                                        for (movieSnapshot in snapshot.children) {
                                            val movies =
                                                movieSnapshot.getValue(MovieDetails::class.java)
                                            if (movies != null) {
                                                if (!mlist.contains(movies)) {
                                                    mlist.add(movies)
                                                }
                                            }
                                        }
                                    }
                                    popShimmerFrameLayout.stopShimmer()
                                    popShimmerFrameLayout.isVisible = false

                                    //recyclerview
                                    popMoviesAdapter = MoviesAdapter(requireContext(), resultList, mlist)
                                    popView.adapter = popMoviesAdapter

                                    popMoviesAdapter.setOnMovieClickListener(object : MovieClickListener {
                                        override fun onMovieClicked(position: Int) {
                                            val bottomSheetFragment = BottomSheet()
                                            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
                                            val bundle = Bundle()
                                            bundle.putString("title", resultList[position].titleText.text)
                                            bundle.putString("year", resultList[position].releaseYear.year.toString())
                                            bundle.putString("poster", resultList[position].primaryImage.url)
                                            parentFragmentManager.setFragmentResult("dataFromAdapter", bundle)
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                            popView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
                catch (e:Exception)
                {
                    Log.i("runCheck",e.toString())
                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                // if api call fails
                Log.d("Home", "onFailure: " + t.message)
            }
        })

        // filters based on genre
        genreDropdown = view.findViewById(R.id.genre_list)
        val genres = retrofitBuilder
                    .create(GenreListInterface::class.java)
                    .getGenres()
        genres.enqueue(object : Callback<Genres?> {

            override fun onResponse(call: Call<Genres?>, response: Response<Genres?>) {
                if(context!=null) {
                    if (response.body()?.results != null) {
                        val list =response.body()!!.results
                        var newList= arrayListOf<String>()
                        for((index,item) in list.withIndex()){
                            if(index!=0)
                                newList.add(item)
                        }
                        val genreListAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.filter_item,
                            newList
                        )
                        genreDropdown.adapter = genreListAdapter
                    } else {
                        Log.d("home fragment", response.errorBody().toString())
                    }
                }
            }

            override fun onFailure(call: Call<Genres?>, t: Throwable) {
                Log.d("Genre", "onFailure: " + t.message)
            }
        })

        genreDropdown.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val genreValue = parent?.getItemAtPosition(position).toString()
        val genre = retrofitBuilder
                    .create(GenreInterface::class.java)
                    .genres(genreValue)

        genre.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                if(context!=null) {
                    val responseBody = response.body()
                    var resultList = responseBody?.results!!

                    val moviesAdapter = MoviesAdapter(requireContext(), resultList, arrayListOf())
                    filterView.adapter = moviesAdapter
                    moviesAdapter.setOnMovieClickListener(object : MovieClickListener {
                        override fun onMovieClicked(position: Int) {
                            val bottomSheetFragment = BottomSheet()
                            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
                            val bundle = Bundle()
                            bundle.putString("title", resultList[position].titleText.text)
                            bundle.putString("year", resultList[position].releaseYear.year.toString()
                            )
                            if (resultList[position].primaryImage.url == null){
                                resultList[position].primaryImage.url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlZSOLoJ-j_JMIlVZZAlQg3R6_y8Jvq4QNumJl1fbowA&s"
                            }
                            bundle.putString("poster", resultList[position].primaryImage.url)
                            parentFragmentManager.setFragmentResult("dataFromAdapter", bundle)
                        }
                    })
                    filterView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Log.d("FilterFailure:", "" + t.message)
            }
        })
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    // 3 dots implementation
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