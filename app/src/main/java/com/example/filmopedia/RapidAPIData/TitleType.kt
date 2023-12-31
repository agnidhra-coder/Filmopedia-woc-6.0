package com.example.filmopedia.RapidAPIData

data class TitleType(
    val __typename: String,
    val canHaveEpisodes: Boolean,
    val categories: List<Category>,
    val displayableProperty: DisplayableProperty,
    val id: String,
    val isEpisode: Boolean,
    val isSeries: Boolean,
    val text: String
)