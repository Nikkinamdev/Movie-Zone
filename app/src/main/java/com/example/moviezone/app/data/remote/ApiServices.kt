package com.example.moviezone.app.data.remote

import com.example.moviezone.app.data.model.MovieDetails
import com.example.moviezone.app.data.model.MoviesResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MoviesResponse
    @GET("movie/{movie_id}")
   suspend fun getMoviesDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String

    ): MovieDetails
}