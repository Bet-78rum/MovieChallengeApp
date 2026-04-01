package com.ali.TrendMovie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
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
import com.ali.TrendMovie.api.RetrofitInstance
import com.ali.TrendMovie.model.Movie
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onShowAllClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    isTurkish: Boolean, // Yeni: Dil durumu
    onToggleLanguage: () -> Unit // Yeni: Dil değiştirme fonksiyonu
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

    // Dil değiştiğinde verileri tekrar çekmek için 'isTurkish' bağımlılığını ekledik
    LaunchedEffect(isTurkish) {
        try {
            val apiKey = "3718ca57be56d9dae8c1ec94fde249db"
            val lang = if (isTurkish) "tr-TR" else "en-US"
            
            nowPlaying.value = RetrofitInstance.api.getNowPlayingMovies(apiKey, lang).results
            popular.value = RetrofitInstance.api.getPopularMovies(apiKey, lang).results
            topRated.value = RetrofitInstance.api.getTopRatedMovies(apiKey, lang).results
            upcoming.value = RetrofitInstance.api.getUpcomingMovies(apiKey, lang).results
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
                            placeholder = { Text(if (isTurkish) "Film Ara..." else "Search Movie...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(if (isTurkish) "Trend Film" else "Trend Movie")
                    }
                },
                actions = {
                    // Dil Değiştirme Butonu (Dünya ikonu)
                    IconButton(onClick = onToggleLanguage) {
                        Icon(
                            imageVector = Icons.Filled.Language, 
                            contentDescription = "Language",
                            tint = if (isTurkish) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorites", tint = MaterialTheme.colorScheme.primary)
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
                        title = if (isTurkish) "Şu An Oynatılanlar" else "Now Playing",
                        movies = nowPlaying.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("now_playing") }
                    )
                    CategorySection(
                        title = if (isTurkish) "Popüler" else "Popular",
                        movies = popular.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("popular") }
                    )
                    CategorySection(
                        title = if (isTurkish) "En Çok Oylananlar" else "Most Voted",
                        movies = topRated.value.filter { it.title.contains(searchQuery, true) },
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick("top_rated") }
                    )
                    CategorySection(
                        title = if (isTurkish) "Yakında" else "Upcoming",
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
