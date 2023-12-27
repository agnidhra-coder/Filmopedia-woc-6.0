package com.example.filmopedia

import com.example.filmopedia.tmdbData.tmdbData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterfaceTmdb {
    @GET("3/movie/popular?api_key=1c0244746d0a8f37acdd183e666b526e")
    fun getPopularData() : Call<tmdbData>
}