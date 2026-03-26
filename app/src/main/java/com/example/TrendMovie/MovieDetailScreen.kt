package com.betul.TrendMovie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.betul.TrendMovie.model.Movie

@Composable
fun MovieDetailScreen(movie: Movie, onClose: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
            Text("Close", color = MaterialTheme.colorScheme.primary)
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