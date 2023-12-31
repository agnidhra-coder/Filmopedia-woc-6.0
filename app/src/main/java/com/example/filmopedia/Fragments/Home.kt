package com.example.filmopedia.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmopedia.ApiInterfaceRapid
import com.example.filmopedia.PopMoviesAdapter
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.SearchData
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Home : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var popMoviesAdapter: PopMoviesAdapter
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
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

        shimmerFrameLayout = view.findViewById(R.id.shimmerView)
        shimmerFrameLayout.startShimmer()

        recyclerView = view.findViewById(R.id.popMoviesRecyclerView)

        val baseUrl = "https://moviesdatabase.p.rapidapi.com/"
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterfaceRapid::class.java)

        val rapidRetrofitData = retrofitBuilder.getPopularData()

        rapidRetrofitData.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                // if api call is success, use data from API in app
                val responseBody = response.body()
                val resultList = responseBody?.results!!

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.isVisible = false
                popMoviesAdapter = PopMoviesAdapter(context!!, resultList)
                recyclerView.adapter = popMoviesAdapter
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                // if api call fails
                Log.d("Home", "onFailure: " + t.message)
            }
        })
    }
}