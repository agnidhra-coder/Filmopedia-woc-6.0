package com.example.filmopedia.RapidAPIData

data class PrimaryImage(
    val __typename: String,
    val caption: Caption,
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)