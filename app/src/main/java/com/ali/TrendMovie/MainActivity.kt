package com.ali.TrendMovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ali.TrendMovie.model.Movie
import com.ali.TrendMovie.ui.theme.TrendMovieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val favoriteMovies = remember { mutableStateListOf<Movie>() }

            TrendMovieTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var currentScreen by remember { mutableStateOf("greeting") }
                    var selectedCategory by remember { mutableStateOf<String?>(null) }
                    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

                    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                        when (currentScreen) {
                            "detail" -> {
                                selectedMovie?.let { movie ->
                                    MovieDetailScreen(
                                        movie = movie,
                                        isFavorite = favoriteMovies.any { it.id == movie.id },
                                        onToggleFavorite = {
                                            if (favoriteMovies.any { it.id == movie.id }) {
                                                favoriteMovies.removeAll { it.id == movie.id }
                                            } else {
                                                favoriteMovies.add(movie)
                                            }
                                        },
                                        onClose = { currentScreen = "home" }
                                    )
                                }
                            }

                            "category" -> {
                                selectedCategory?.let { category ->
                                    ShowAllScreen(
                                        category = category,
                                        onMovieClick = { movie ->
                                            selectedMovie = movie
                                            currentScreen = "detail"
                                        },
                                        onBack = { currentScreen = "home" }
                                    )
                                }
                            }

                            "favorites" -> {
                                FavoritesScreen(
                                    movies = favoriteMovies,
                                    onMovieClick = { movie ->
                                        selectedMovie = movie
                                        currentScreen = "detail"
                                    },
                                    onBack = { currentScreen = "home" }
                                )
                            }

                            "home" -> {
                                HomeScreen(
                                    onMovieClick = { movie ->
                                        selectedMovie = movie
                                        currentScreen = "detail"
                                    },
                                    onShowAllClick = { category ->
                                        selectedCategory = category
                                        currentScreen = "category"
                                    },
                                    onHomeClick = { currentScreen = "greeting" },
                                    onFavoritesClick = { currentScreen = "favorites" },
                                    isDarkMode = isDarkMode,
                                    onToggleDarkMode = { isDarkMode = !isDarkMode }
                                )
                            }

                            else -> {
                                Greeting(onClick = { currentScreen = "home" })
                            }
                        }
                    }
                }
            }
        }
    }
}
