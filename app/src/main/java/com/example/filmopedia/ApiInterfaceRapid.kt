package com.example.filmopedia

import com.example.filmopedia.RapidAPIData.SearchData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiInterfaceRapid {
    @GET("titles?list=top_boxoffice_200&limit=20")
    @Headers(
        "X-RapidAPI-Key: ebebce4ca8msh1022a37d8630962p19f74ajsn382b09e49349",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )
    fun getPopularData() : Call<SearchData>
}