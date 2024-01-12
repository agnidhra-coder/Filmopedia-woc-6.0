package com.example.filmopedia

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.filmopedia.RapidAPIData.SearchData
import com.example.filmopedia.interfaces.SearchInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestManager(var context: Context) {
    val baseUrl = "https://moviesdatabase.p.rapidapi.com/"
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun searchMovies(listener: SearchListener, movieName : String){
        val getMovies = retrofitBuilder.create(SearchInterface::class.java)
        val call = getMovies.callMovies(movieName)

        call.enqueue(object : Callback<SearchData?> { // Ctrl+Shift+Spacebar for magic
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
//                try{
            if(!response.isSuccessful()){
                Toast.makeText(context, "Couldn't fetch data", Toast.LENGTH_SHORT).show()
            }
            listener.onResponse(response.body())
//                }
//                catch (e: Exception){
//                    Toast.makeText(context, "No movie found", Toast.LENGTH_SHORT).show()
//                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Log.d("Search", "Error: " + t.message)
                listener.onError(t.message!!)
            }
        })
    }
}