package com.example.filmopedia.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.interfaces.MovieApiInterface
import com.example.filmopedia.Adapters.MoviesAdapter
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.RapidAPIData.genres.Genres
import com.example.filmopedia.interfaces.GenreInterface
import com.example.filmopedia.interfaces.GenreListInterface
import com.example.filmopedia.interfaces.ItemClickListener
import com.facebook.shimmer.ShimmerFrameLayout
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
    private val baseUrl = "https://moviesdatabase.p.rapidapi.com/"
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popShimmerFrameLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerView)
        popShimmerFrameLayout.startShimmer()

        popView = view.findViewById(R.id.popMoviesRecyclerView)
        filterView = view.findViewById(R.id.filterMoviesRecyclerView)

        val rapidRetrofitData = retrofitBuilder
                                .create(MovieApiInterface::class.java)
                                .getPopularData()

        rapidRetrofitData.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                // if api call is success, use data from API in app
                val responseBody = response.body()
                val resultList = responseBody?.results!!

                popShimmerFrameLayout.stopShimmer()
                popShimmerFrameLayout.isVisible = false

                //recyclerview
                popMoviesAdapter = MoviesAdapter(context!!, resultList)
                popView.adapter = popMoviesAdapter
                popMoviesAdapter.setOnItemClickListener(object: ItemClickListener{
                    override fun onItemClick(position: Int) {
                        val bottomSheetFragment = BottomSheet()
                        bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
                    }
                })
                popView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
                val genreListAdapter  = ArrayAdapter(context!!, R.layout.filter_item, response.body()?.results!!)
                genreDropdown.adapter = genreListAdapter
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

//        val filterShimmerFrameLayout = view?.findViewById<ShimmerFrameLayout>(R.id.filterShimmerView)
//        filterShimmerFrameLayout!!.startShimmer()

        genre.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
//                filterShimmerFrameLayout.stopShimmer()
//                filterShimmerFrameLayout.isVisible = false

                val responseBody = response.body()
                val resultList = responseBody?.results!!

                val moviesAdapter = MoviesAdapter(context!!, resultList)
                filterView.adapter = moviesAdapter
                filterView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Log.d("FilterFailure:", "" + t.message)
            }
        })


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}