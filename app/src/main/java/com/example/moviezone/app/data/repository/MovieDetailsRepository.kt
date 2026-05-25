package com.example.moviezone.app.data.repository

import android.util.Log
import com.example.moviezone.app.data.model.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MovieDetailsRepository {

    fun getMovieDetails(
        movieId: Int,
        apiKey: String
    ): Flow<MovieDetails?> = flow {

        try {

            Log.d("DETAIL_API", "API CALL STARTED")

            val response = RetrofitClient.api.getMoviesDetails(movieId, apiKey)

            Log.d("DETAIL_SUCCESS", response.toString())

            emit(response)

        } catch (e: Exception) {

            Log.e("DETAIL_ERROR", e.message.toString())

            emit(null)
        }
    }
}