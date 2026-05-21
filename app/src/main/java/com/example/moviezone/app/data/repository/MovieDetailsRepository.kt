package com.example.moviezone.app.data.repository

import android.util.Log
import com.example.moviezone.app.data.model.MovieDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsRepository {

    fun getMovieDetails(
        movieId: Int,
        apiKey: String,
        callback: (MovieDetails?) -> Unit
    ) {

        Log.d("DETAIL_API", "API CALL STARTED")

        RetrofitClient.api.getMoviesDetails(movieId, apiKey)
            .enqueue(object : Callback<MovieDetails> {

                override fun onResponse(
                    call: Call<MovieDetails>,
                    response: Response<MovieDetails>
                ) {

                    Log.e("DETAIL_CODE", response.code().toString())

                    if (response.isSuccessful && response.body() != null) {

                        Log.d("DETAIL_SUCCESS", response.body().toString())

                        callback(response.body())

                    } else {

                        Log.e(
                            "DETAIL_ERROR",
                            response.errorBody()?.string().toString()
                        )

                        callback(null)
                    }
                }

                override fun onFailure(
                    call: Call<MovieDetails>,
                    t: Throwable
                ) {

                    Log.e("DETAIL_FAILURE", t.message.toString())

                    callback(null)
                }
            })
    }
}