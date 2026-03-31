package com.ali.TrendMovie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ali.TrendMovie.model.Movie

@Composable
fun CategorySection(title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit, onShowAllClick: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text("View All >", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { onShowAllClick() })
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(movies) { movie ->
                MovieItem(movie, onMovieClick)
            }
        }
    }
}