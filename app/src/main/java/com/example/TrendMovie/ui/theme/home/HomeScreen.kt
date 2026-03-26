package com.betul.TrendMovie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.betul.TrendMovie.api.RetrofitInstance
import com.betul.TrendMovie.model.Movie
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onShowAllClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            isConnected = isInternetAvailable(context)
            delay(2000)
        }
    }

    val nowPlaying = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val popular = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val topRated = remember { mutableStateOf<List<Movie>>(emptyOf()) }
    val upcoming = remember { mutableStateOf<List<Movie>>(emptyList()) }

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val apiKey = "3718ca57be56d9dae8c1ec94fde249db"
            nowPlaying.value = RetrofitInstance.api.getNowPlayingMovies(apiKey).results
            popular.value = RetrofitInstance.api.getPopularMovies(apiKey).results
            topRated.value = RetrofitInstance.api.getTopRatedMovies(apiKey).results
            upcoming.value = RetrofitInstance.api.getUpcomingMovies(apiKey).results
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Movie...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Trend Movie")
                    }
                },
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorites", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onHomeClick) {
                        Icon(Icons.Filled.Home, contentDescription = "Home Page")
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NoInternetWarning(isConnected)
            LazyColumn {
                item {
                    CategorySection(
                        title = "Now Playing",
                        movies = nowPlaying.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("now_playing") }
                    )
                    CategorySection(
                        title = "Popular",
                        movies = popular.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("popular") }
                    )
                    CategorySection(
                        title = "Most Voted",
                        movies = topRated.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("top_rated") }
                    )
                    CategorySection(
                        title = "Soon",
                        movies = upcoming.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("upcoming") }
                    )
                }
            }
        }
    }
}

private fun <T> emptyOf(): List<T> = emptyList()
