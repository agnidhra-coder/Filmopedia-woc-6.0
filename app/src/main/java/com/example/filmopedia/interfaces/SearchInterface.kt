package com.example.filmopedia.interfaces

import com.example.filmopedia.RapidAPIData.SearchData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchInterface {
    @Headers(
        "X-RapidAPI-Key: ebebce4ca8msh1022a37d8630962p19f74ajsn382b09e49349",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )

    @GET("titles/search/title/{movie_name}")
    fun callMovies(
        @Path("movie_name") movieName : String,
        @Query("exact") bool : String = "false",
        @Query("sort") sort: String = "year.decr",
        @Query("titleType") titleType : String = "movie"
    ) : Call<SearchData>
}