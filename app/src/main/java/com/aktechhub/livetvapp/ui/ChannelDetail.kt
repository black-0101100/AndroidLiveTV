package com.aktechhub.livetvapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun ChannelDetailScreen(
    channelNumber: String,
    channelName: String,
    channelLogoUrl: String,
    onTimeout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.7f)) // Semi-transparent background
    ) {
        // Channel Details Row
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart) // Align to the bottom-left corner
                .padding(16.dp), // Add padding for spacing
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Channel Logo
            AsyncImage(
                model = channelLogoUrl,
                contentDescription = "Channel Logo",
                modifier = Modifier
                    .size(60.dp) // Adjust size as needed
                    .padding(end = 16.dp) // Add spacing between logo and text
            )

            // Channel Number and Name
            Column {
                Text(
                    text = channelNumber,
                    color = Color.White,
                    fontSize = 22.sp, // Adjust font size
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = channelName,
                    color = Color.White,
                    fontSize = 18.sp, // Adjust font size
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Auto-hide after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000)
        onTimeout()
    }
}