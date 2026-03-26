package com.betul.TrendMovie

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.betul.TrendMovie.model.Movie

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