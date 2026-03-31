package com.ali.TrendMovie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                    text = " Your internet is off. Please turn it on.",
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}