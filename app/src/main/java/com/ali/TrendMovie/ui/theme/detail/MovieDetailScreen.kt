package com.ali.TrendMovie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ali.TrendMovie.model.Movie
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

@Composable
fun MovieDetailScreen(
    movie: Movie,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        
        // 1. TÜM EKRAN SİLİNİK ARKA PLAN
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.40f
        )

        // 2. İÇERİK
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Üst Butonlar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ANA RESİM
            AsyncImage(
                model = "https://image.tmdb.org/t/p/original${movie.poster_path}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // FİLM BİLGİLERİ VE BEYAZ ARKA PLAN
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.7f), // Hafif şeffaf beyaz çok daha şık durur
                tonalElevation = 4.dp,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = movie.title, 
                        style = MaterialTheme.typography.headlineLarge, // Yazıyı büyüttük
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Score: ⭐ ${movie.vote_average}", 
                        style = MaterialTheme.typography.titleLarge, // Yazıyı büyüttük
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.headlineSmall, // Başlığı büyüttük
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.overview, 
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp), // Yazıyı büyüttük
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
