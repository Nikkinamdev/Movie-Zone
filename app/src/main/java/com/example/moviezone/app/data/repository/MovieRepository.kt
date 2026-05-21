package com.example.moviezone.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviezone.app.data.local.FavoriteMovie
import com.example.moviezone.app.data.local.MovieDao
import com.example.moviezone.app.data.model.Movies
import com.example.moviezone.app.data.model.MoviesResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(
    private val dao: MovieDao
) {

    fun getMovies(apiKey: String, callback: (List<Movies>) -> Unit) {

        Log.d("API_DEBUG", "API CALL STARTED")

        RetrofitClient.api.getDiscoverMovies(apiKey)
            .enqueue(object : Callback<MoviesResponse> {

                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    Log.e("API_CODE", response.code().toString())

                    if (response.isSuccessful && response.body() != null) {
                        Log.e("Api response", response.body().toString())
                        val moviesList = response.body()!!.results
                        Log.d("API_SUCCESS", "size = ${moviesList.size}")

                        callback(moviesList)

                    } else {

                        Log.e("API_ERROR", response.errorBody()?.string().toString())
                        callback(emptyList())
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    Log.e("API_DEBUG", "FAILURE = ${t.message}")
                    callback(emptyList())
                }
            })
    }

    suspend fun saveFavorite(movie: FavoriteMovie) {
        dao.insertMovie(movie)
    }
    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return dao.getAllFavorites()
    }
    suspend fun deleteFavorite(movie: FavoriteMovie) {
        dao.deleteMovie(movie)
    }
    suspend fun logoutfavdata(){
        dao.clearFavMovies()

    }


}
