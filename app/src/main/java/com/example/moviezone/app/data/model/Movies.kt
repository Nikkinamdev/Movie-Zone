package com.example.moviezone.app.data.model

data class Movies(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val overview: String,
    val vote_average: Double,
    val release_date: String,
    val genre_ids: List<Int>
)