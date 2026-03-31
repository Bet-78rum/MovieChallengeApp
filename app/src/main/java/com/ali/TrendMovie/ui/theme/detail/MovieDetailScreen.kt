package com.ali.TrendMovie

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ali.TrendMovie.model.Movie
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

@Composable
fun MovieDetailScreen(
    movie: Movie,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onClose) {
                Text("Close", color = MaterialTheme.colorScheme.primary)
            }
        }
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxWidth().height(350.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Score: ⭐ ${movie.vote_average}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview)
    }
}
