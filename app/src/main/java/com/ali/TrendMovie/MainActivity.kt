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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ali.TrendMovie.model.Movie

// Ekranların importları - Eğer hata alırsan bu yolları kontrol etmelisin
// Not: Tüm dosyaların 'package com.ali.TrendMovie' olarak tanımlandığını varsayıyorum.

import com.ali.TrendMovie.ui.theme.TrendMovieTheme

// Navigation 3 için anahtar (route) tanımları
sealed interface NavKey {
    data object Greeting : NavKey
    data object Home : NavKey
    data class MovieDetail(val movie: Movie) : NavKey
    data class ShowAll(val category: String) : NavKey
    data object Favorites : NavKey
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val favoriteMovies = remember { mutableStateListOf<Movie>() }

            TrendMovieTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    
                    // Navigation 3 Back Stack (Liste tabanlı)
                    val backStack = remember { mutableStateListOf<NavKey>(NavKey.Greeting) }

                    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                        NavDisplay(
                            backStack = backStack,
                            onBack = { 
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                } else {
                                    finish()
                                }
                            },
                            entryProvider = { key ->
                                when (key) {
                                    is NavKey.Greeting -> NavEntry(key) {
                                        Greeting(onClick = { 
                                            backStack.add(NavKey.Home) 
                                        })
                                    }
                                    is NavKey.Home -> NavEntry(key) {
                                        HomeScreen(
                                            onMovieClick = { movie -> 
                                                backStack.add(NavKey.MovieDetail(movie)) 
                                            },
                                            onShowAllClick = { category -> 
                                                backStack.add(NavKey.ShowAll(category)) 
                                            },
                                            onHomeClick = { 
                                                if (backStack.isNotEmpty() && backStack.last() !is NavKey.Greeting) {
                                                    backStack.clear()
                                                    backStack.add(NavKey.Greeting)
                                                }
                                            },
                                            onFavoritesClick = { 
                                                backStack.add(NavKey.Favorites) 
                                            },
                                            isDarkMode = isDarkMode,
                                            onToggleDarkMode = { isDarkMode = !isDarkMode }
                                        )
                                    }
                                    is NavKey.MovieDetail -> NavEntry(key) {
                                        val movie = key.movie
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
                                            onClose = { backStack.removeAt(backStack.size - 1) }
                                        )
                                    }
                                    is NavKey.ShowAll -> NavEntry(key) {
                                        ShowAllScreen(
                                            category = key.category,
                                            onMovieClick = { movie -> 
                                                backStack.add(NavKey.MovieDetail(movie)) 
                                            },
                                            onBack = { backStack.removeAt(backStack.size - 1) }
                                        )
                                    }
                                    is NavKey.Favorites -> NavEntry(key) {
                                        FavoritesScreen(
                                            movies = favoriteMovies,
                                            onMovieClick = { movie -> 
                                                backStack.add(NavKey.MovieDetail(movie)) 
                                            },
                                            onBack = { backStack.removeAt(backStack.size - 1) }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
