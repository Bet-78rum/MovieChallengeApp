package com.example.moviechallengeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviechallengeapp.api.RetrofitInstance
import com.example.moviechallengeapp.model.Movie
import com.example.moviechallengeapp.ui.theme.MovieChallengeAppTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Divider
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            MovieChallengeAppTheme(darkTheme = isDarkMode) {
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
@Composable
fun Greeting(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onClick) {
            Text(text = "Filmleri Keşfet")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    onShowAllClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            isConnected = isInternetAvailable(context)
            delay(2000) // 2 saniyede bir kontrol
        }
    }

    val nowPlaying = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val popular = remember { mutableStateOf<List<Movie>>(emptyList()) }
    val topRated = remember { mutableStateOf<List<Movie>>(emptyList()) }
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
                            placeholder = { Text("Film ara...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Filmler")
                    }
                },
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(Icons.Filled.Search, contentDescription = "Arama")
                    }
                    IconButton(onClick = onHomeClick) {
                        Icon(Icons.Filled.Home, contentDescription = "Ana Sayfa")
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
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
                    title = "Top Rated",
                    movies = topRated.value.filter { it.title.contains(searchQuery, true) },
                    onMovieClick = onMovieClick,
                    onShowAllClick = { onShowAllClick("top_rated") }
                )
                CategorySection(
                    title = "Upcoming",
                    movies = upcoming.value.filter { it.title.contains(searchQuery, true) },
                    onMovieClick = onMovieClick,
                    onShowAllClick = { onShowAllClick("upcoming") }
                )
            }
        }
    }
}
@Composable
fun MovieDetailScreen(movie: Movie, onClose: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
            Text("Kapat", color = MaterialTheme.colorScheme.primary)
        }
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxWidth().height(350.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Puan: ⭐ ${movie.vote_average}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAllScreen(category: String, onMovieClick: (Movie) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val isConnected = remember {
        isInternetAvailable(context)
    }
    NoInternetWarning(isConnected)
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
    NoInternetWarning(isConnected)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.replace("_", " ").uppercase()) },
                navigationIcon = {
                    Button(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text("Geri")
                    }
                }
            )
        }
    )

    { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
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
                            text = "Puan: ⭐ ${movie.vote_average}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }
    }
}
@Composable
fun CategorySection(title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit, onShowAllClick: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text("Hepsini Gör >", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { onShowAllClick() })
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(movies) { movie ->
                MovieItem(movie, onMovieClick)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit) {
    Column(modifier = Modifier.width(150.dp).padding(8.dp).clickable { onClick(movie) }) {
        Card(shape = RoundedCornerShape(8.dp)) {
            AsyncImage(model = "https://image.tmdb.org/t/p/w500${movie.poster_path}", contentDescription = null, modifier = Modifier.height(200.dp), contentScale = ContentScale.Crop)
        }
        Text(text = movie.title, maxLines = 1, modifier = Modifier.padding(top = 4.dp))
    }
}
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
@Composable
fun NoInternetWarning(isConnected: Boolean) {
    if (!isConnected) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = " İnternetiniz kapalı. Lütfen açın.",
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}



