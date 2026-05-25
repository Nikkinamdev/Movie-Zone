package com.example.moviezone.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviezone.app.data.local.FavoriteMovie
import com.example.moviezone.app.data.local.MovieDao
import com.example.moviezone.app.data.model.Movies

import com.example.moviezone.app.data.model.MoviesResponse
import retrofit2.awaitResponse

class MovieRepository(
    private val dao: MovieDao
) {

    suspend fun getMovies(apiKey: String): List<Movies> {

        Log.d("API_DEBUG", "API CALL STARTED")

        val response = RetrofitClient.api.getDiscoverMovies(apiKey,1)

        val moviesList = response.results

        Log.d("API_SUCCESS", "size = ${moviesList.size}")

        return moviesList
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
