package com.example.filmopedia.RapidAPIData

data class SearchData(
    val entries: Int,
    val next: String,
    val page: Int,
    val results: List<Result>
)