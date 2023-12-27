package com.example.filmopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import com.example.filmopedia.tmdbData.tmdbData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterfaceTmdb::class.java)

        // tmdb
        val tmdbRetrofitData = retrofitBuilder.getPopularData()

        tmdbRetrofitData.enqueue(object : Callback<tmdbData?> {
            override fun onResponse(call: Call<tmdbData?>, response: Response<tmdbData?>) {
                // if api call is success, use data from API in app
            }

            override fun onFailure(call: Call<tmdbData?>, t: Throwable) {
                // if api call fails
                Log.d("Main Activity", "onFailure: " + t.message)
            }
        })
    }
}