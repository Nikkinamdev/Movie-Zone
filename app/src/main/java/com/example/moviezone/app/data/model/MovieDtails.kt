package com.example.moviezone.app.data.model
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Double,
    val runtime: Int,
    val release_date: String,
    val original_language: String,
    val tagline: String
)