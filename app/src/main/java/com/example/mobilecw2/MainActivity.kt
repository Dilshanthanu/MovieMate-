package com.example.mobilecw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecw2.Routes.LandingPage
import com.example.mobilecw2.appuis.MainScreen
import com.example.mobilecw2.appuis.SearchActors
import com.example.mobilecw2.appuis.SearchMovie
import com.example.mobilecw2.appuis.WebSearchScreen
import com.example.mobilecw2.ui.theme.MobileCw2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        setContent {
            MobileCw2Theme {
                Scaffold(

                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        App(modifier = Modifier.padding(innerPadding) , viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier , viewModel: MovieViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LandingPage) {
        composable(LandingPage) {
            MainScreen(viewModel ,navController)
        }
        composable(Routes.SearchMovieScreen) {
            SearchMovie(viewModel)
        }
        composable(Routes.SearchActorsScreen) {
            SearchActors(viewModel)
        }
        composable(Routes.WebSearchScreen) {
            WebSearchScreen(viewModel)
        }

    }
}
