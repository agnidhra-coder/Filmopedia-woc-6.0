package com.example.filmopedia.interfaces

import com.example.filmopedia.RapidAPIData.genres.Genres
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface GenreListInterface {
    @Headers(
        "X-RapidAPI-Key: ebebce4ca8msh1022a37d8630962p19f74ajsn382b09e49349",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )

    @GET("titles/utils/genres")
    fun getGenres(
    ) : Call<Genres>
}