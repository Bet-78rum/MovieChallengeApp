package com.ali.TrendMovie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ali.TrendMovie.api.RetrofitInstance
import com.ali.TrendMovie.model.Movie
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAllScreen(category: String, onMovieClick: (Movie) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            isConnected = isInternetAvailable(context)
            delay(2000)
        }
    }

    val movies = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val apiKey = "3718ca57be56d9dae8c1ec94fde249db"

    LaunchedEffect(category) {
        try {
            val response = when (category) {
                "now_playing" -> RetrofitInstance.api.getNowPlayingMovies(apiKey)
                "popular" -> RetrofitInstance.api.getPopularMovies(apiKey)
                "top_rated" -> RetrofitInstance.api.getTopRatedMovies(apiKey)
                "upcoming" -> RetrofitInstance.api.getUpcomingMovies(apiKey)
                else -> null
            }
            movies.value = response?.results ?: emptyList()
        } catch (e: Exception) { e.printStackTrace() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when(category) {
                            "now_playing" -> "Those in the vision"
                            "popular" -> "Popular"
                            "top_rated" -> "Most Voted"
                            "upcoming" -> "Soon"
                            else -> category.replace("_", " ").uppercase()
                        }
                    )
                },
                navigationIcon = {
                    // "Back" butonu yerine Geri Oku (Back Arrow) ikonu
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            NoInternetWarning(isConnected)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(movies.value) { movie ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMovieClick(movie) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 60.dp, height = 90.dp)
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w200${movie.poster_path}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                            Text(
                                text = "Score: ⭐ ${movie.vote_average}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                }
            }
        }
    }
}