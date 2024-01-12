package com.example.filmopedia

import com.example.filmopedia.RapidAPIData.SearchData

interface SearchListener {
    fun onResponse(response: SearchData?)
    fun onError(msg : String)
}