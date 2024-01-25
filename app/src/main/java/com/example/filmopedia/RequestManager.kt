package com.example.filmopedia

import android.content.Context
import android.util.Log
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

        call.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                try{
                    if(!response.isSuccessful()){
                        {}
                    }
                    if(response.body()!=null) {
                        listener.onResponse(response.body())
                    }
                }
                catch (e: Exception){
                    {}
                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Log.d("Search", "Error: " + t.message)
                listener.onError(t.message!!)
            }
        })
    }
}