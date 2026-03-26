package com.betul.TrendMovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.betul.TrendMovie.model.Movie
import com.betul.TrendMovie.ui.theme.TrendMovieTheme
import androidx.compose.foundation.layout.statusBarsPadding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            TrendMovieTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var showMovies by remember { mutableStateOf(false) }
                    var selectedCategory by remember { mutableStateOf<String?>(null) }
                    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

                    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                        when {
                            selectedMovie != null -> {
                                MovieDetailScreen(
                                    movie = selectedMovie!!,
                                    onClose = { selectedMovie = null }
                                )
                            }
                            selectedCategory != null -> {
                                ShowAllScreen(
                                    category = selectedCategory!!,
                                    onMovieClick = { movie -> selectedMovie = movie },
                                    onBack = { selectedCategory = null }
                                )
                            }
                            showMovies -> {
                                HomeScreen(
                                    onMovieClick = { movie -> selectedMovie = movie },
                                    onShowAllClick = { category -> selectedCategory = category },
                                    onHomeClick = { showMovies = false },
                                    isDarkMode = isDarkMode,
                                    onToggleDarkMode = { isDarkMode = !isDarkMode }
                                )
                            }
                            else -> {
                                Greeting(onClick = { showMovies = true })
                            }
                        }
                    }
                }
            }
        }
    }
}
