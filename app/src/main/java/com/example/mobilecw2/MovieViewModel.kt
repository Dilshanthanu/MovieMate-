package com.example.mobilecw2

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecw2.models.Movie
import com.example.mobilecw2.models.WebSearch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MovieViewModel : ViewModel() {

    private var _movies = mutableStateOf<List<Movie>>(emptyList())
    private var _searchResult = mutableStateOf<List<Movie>>(emptyList())
    private var _searchTitles = mutableStateOf<List<WebSearch>>(emptyList())

    val movies: List<Movie> get() = _movies.value
    val searchResult: List<Movie> get() = _searchResult.value
    val searchTitles: List<WebSearch> get() = _searchTitles.value

    val API_KEY = "13ef0d29"
    val movieDAO = MainApplication.database.getMovieDAO()

    fun loadMoviesFromAssets(context: Context): List<Movie> {
        val inputStream = context.assets.open("movie.json")
        val reader = InputStreamReader(inputStream)
        val movieType = object : TypeToken<List<Movie>>() {}.type
        return Gson().fromJson(reader, movieType)
    }

    fun addMoviesToDb(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val movies = loadMoviesFromAssets(context)
            for (movie in movies) {
                Log.d("MovieCheck", movie.toString())
                movieDAO.addTodoItem(movie)
            }
        }
    }

    fun fetchData(
        onMovieFetched: (List<Movie>) -> Unit,
        isLoadingStatus: (Boolean) -> Unit,
        MovieName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (MovieName.isBlank()) {
                    withContext(Dispatchers.Main) {
                        isLoadingStatus(false)
                    }
                    return@launch
                }

                isLoadingStatus(true)

                val encodedMovieName = URLEncoder.encode(MovieName, "UTF-8")
                val url = URL("https://www.omdbapi.com/?t=$encodedMovieName&apikey=$API_KEY")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonResponse = connection.inputStream.bufferedReader().use { it.readText() }
                    val decodedMovie = parseMovies(jsonResponse)

                    withContext(Dispatchers.Main) {
                        onMovieFetched(decodedMovie)
                        isLoadingStatus(false)
                    }
                } else {
                    Log.e("fetchData", "HTTP Error code: $responseCode")
                    withContext(Dispatchers.Main) {
                        isLoadingStatus(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("fetchData", "Error fetching data: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    isLoadingStatus(false)
                }
            }
        }
    }


    fun parseMovies(jsonResponse: String): List<Movie> {

        _movies.value = emptyList()

        val movieJson = org.json.JSONObject(jsonResponse)

        val title = movieJson.optString("Title", null)
        val year = movieJson.optString("Year", null)
        val rated = movieJson.optString("Rated", null)
        val released = movieJson.optString("Released", null)
        val runtime = movieJson.optString("Runtime", null)
        val genre = movieJson.optString("Genre", null)
        val director = movieJson.optString("Director", null)
        val writer = movieJson.optString("Writer", null)
        val actors = movieJson.optString("Actors", null)
        val plot = movieJson.optString("Plot", null)
        val poster = movieJson.optString("Poster", null)

        val movie = Movie(
            title = title,
            year = year,
            rated = rated,
            released = released,
            runtime = runtime,
            genre = genre,
            director = director,
            writer = writer,
            actors = actors,
            plot = plot,
            poster = poster,

            )

        _movies.value = listOf(movie)

        return _movies.value
    }


    fun SaveSearchMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            for (movie in _movies.value) {
                movieDAO.addTodoItem(movie)
            }
        }
    }

    fun SearchMoviesByActor(actorName: String) {
        viewModelScope.launch {
            Log.d("Search", "Searching for: ${actorName}")
            val results = movieDAO.searchMoviesByActor(actorName.trim())
            Log.d("Search", "Found: ${results.size} this is the result ${results}")

            _searchResult.value = results
        }
    }

    fun searchTitlesFromWeb(
        isLoadingStatus: (Boolean) -> Unit,
        MovieName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (MovieName.isBlank()) {
                    withContext(Dispatchers.Main) {
                        isLoadingStatus(false)
                    }
                    return@launch
                }

                isLoadingStatus(true)


                val url = URL("https://www.omdbapi.com/?s=$MovieName&apikey=$API_KEY")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonResponse =
                        connection.inputStream.bufferedReader().use { it.readText() }
                    val decodedMovie = parseWebSearch(jsonResponse)

                    withContext(Dispatchers.Main) {
                        isLoadingStatus(false)
                    }
                } else {
                    Log.e("fetchData", "HTTP Error code: $responseCode")
                    withContext(Dispatchers.Main) {
                        isLoadingStatus(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("fetchData", "Error fetching data: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    isLoadingStatus(false)
                }
            }
        }

    }

    fun parseWebSearch(jsonResponse: String): List<WebSearch> {
        val searchResults = mutableListOf<WebSearch>()
        val movieJson = JSONObject(jsonResponse)

        val searchArray = movieJson.optJSONArray("Search")
        if (searchArray != null) {
            for (i in 0 until searchArray.length()) {
                val item = searchArray.getJSONObject(i)
                val title = item.optString("Title", null)
                val year = item.optString("Year", null)
                val imdbID = item.optString("imdbID", null)
                val type = item.optString("Type", null)
                val poster = item.optString("Poster", null)

                val movie = WebSearch(
                    title = title,
                    year = year,
                    imdbID = imdbID,
                    type = type,
                    poster = poster
                )
                searchResults.add(movie)
            }
        }

        _searchTitles.value = searchResults
        return searchResults
    }



}
