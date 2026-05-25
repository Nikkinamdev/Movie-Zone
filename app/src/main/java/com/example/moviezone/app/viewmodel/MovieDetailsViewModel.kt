// MovieDetailsViewModel.kt

package com.example.moviezone.app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.moviezone.app.data.model.MovieDetails
import com.example.moviezone.app.data.repository.MovieDetailsRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository = MovieDetailsRepository()

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchMovie(movieId: Int) {

        repository.getMovieDetails(movieId, "ccbd7ce8cfbda28dc078738e87059d06") { result ->

            result?.let {
                _movieDetails.postValue(it)
            } ?: run {
                _error.postValue("Failed to load movie details")
            }
        }
    }
}