package com.example.moviezone.app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.moviezone.app.data.local.FavoriteMovie
import com.example.moviezone.app.data.local.MovieDatabase
import com.example.moviezone.app.data.model.Movies
import com.example.moviezone.app.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: MovieRepository

    var isLoading = MutableLiveData<Boolean>()

    val movies = MutableLiveData<List<Movies>>()

    val favoriteMovies: LiveData<List<FavoriteMovie>>

    init {

        val dao = MovieDatabase
            .getDatabase(application)
            .movieDao()

        repository = MovieRepository(dao)

        favoriteMovies = repository.getFavoriteMovies()
    }

    fun loadMovies() {

        isLoading.value = true

        repository.getMovies(
            "ccbd7ce8cfbda28dc078738e87059d06"
        ) { list ->

            isLoading.value = false

            Log.d("VM_DEBUG", "Received size: ${list.size}")

            movies.postValue(list)   // 🔥 IMPORTANT FIX
        }
    }

    fun saveFavorite(movie: FavoriteMovie) {

        viewModelScope.launch {

            repository.saveFavorite(movie)

            Log.d("ROOM_DB", "Movie Saved")
        }
    }
    fun deleteFavorite(movie: FavoriteMovie) {

        viewModelScope.launch {

            repository.deleteFavorite(movie)

            Log.d("ROOM_DB", "Movie Deleted")
        }
    }
    fun logoutfavmovieData() {

        viewModelScope.launch {

            repository.logoutfavdata()

            Log.d("ROOM_DB", "fav Movie Deleted")
        }
    }

}