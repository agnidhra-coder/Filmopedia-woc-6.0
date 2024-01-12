package com.example.filmopedia.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.filmopedia.R
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.interfaces.MovieApiInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BottomSheet : BottomSheetDialogFragment() {
    private lateinit var posterView : ImageView
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
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        posterView = view.findViewById(R.id.moviePoster)

        val movieDetails = retrofitBuilder
                        .create(MovieApiInterface::class.java)
                        .getPopularData()

        movieDetails.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                val responseBody = response.body()
                val resultList = responseBody?.results!!

            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Log.d("BottomSheet", "onFailure: " + t.message)
            }
        })
    }
}