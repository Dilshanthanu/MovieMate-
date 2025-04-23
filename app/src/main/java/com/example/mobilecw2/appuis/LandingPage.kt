package com.example.mobilecw2.appuis

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilecw2.MovieViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(viewModel: MovieViewModel, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "WELCOME",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.addMoviesToDb(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F42B9)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
        ) {
            Text(text = "Add Movies to DB", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("search_movie_screen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F42B9)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
        ) {
            Text(text = "Search for Movies", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("search_actors_screen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F42B9)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
        ) {
            Text(text = "Search for Actors", color = Color.White, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { navController.navigate("web_search_screen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F42B9)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
        ) {
            Text(text = "Search Titles from Web", color = Color.White, fontSize = 18.sp)
        }


    }
}
