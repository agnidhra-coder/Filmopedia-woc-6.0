package com.example.filmopedia

import com.example.filmopedia.RapidAPIData.SearchData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterfaceRapid {
    @GET("")
    fun getProductData() : Call<SearchData>
}