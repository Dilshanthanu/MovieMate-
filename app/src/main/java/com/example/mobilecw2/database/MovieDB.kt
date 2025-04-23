package com.example.mobilecw2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilecw2.dao.MovieDAO
import com.example.mobilecw2.models.Movie

@Database(entities = [Movie::class], version = 3)
abstract class MovieDB : RoomDatabase() {
    companion object {
        const val NAME = "movie.db"
    }

    abstract fun getMovieDAO(): MovieDAO
}