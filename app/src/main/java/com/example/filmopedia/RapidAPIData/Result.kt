package com.example.filmopedia.RapidAPIData

data class Result(
    val _id: String,
    val id: String,
    val originalTitleText: OriginalTitleText,
    val primaryImage: PrimaryImage,
    val releaseDate: ReleaseDate,
    val releaseYear: ReleaseYear,
    val titleText: TitleText,
    val titleType: TitleType
)