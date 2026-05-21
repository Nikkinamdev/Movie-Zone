package com.example.moviezone.app.data.local
import androidx.lifecycle.LiveData
import androidx.room.*





@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovie)

    @Delete
    suspend fun deleteMovie(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): LiveData<List<FavoriteMovie>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
    @Query("DELETE FROM favorite_movies")
    suspend fun clearFavMovies()
}