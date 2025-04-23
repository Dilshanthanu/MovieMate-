package com.example.mobilecw2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilecw2.models.Movie

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Insert
    suspend fun addTodoItem(movie: Movie)

    @Query("SELECT * FROM Movies WHERE id = :id")
    fun getMovie(id: Int): Movie

    @Query("SELECT * FROM movies WHERE LOWER(actors) LIKE '%' || LOWER(:actorName) || '%'")
    suspend fun searchMoviesByActor(actorName: String): List<Movie>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()


}