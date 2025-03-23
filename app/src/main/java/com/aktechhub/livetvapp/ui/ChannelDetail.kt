package com.aktechhub.livetvapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
            .fillMaxSize() // Fill the entire screen to allow positioning
            .background(Color.Transparent), // No background here, we'll apply it to the Card
        contentAlignment = Alignment.BottomCenter // Place the content at the bottom center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f) // Take 50% of the screen width
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp), // Padding around the card, with bottom padding to avoid overlapping with any ticker
            shape = RoundedCornerShape(12.dp), // Rounded corners for the box
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.6f) // Semi-transparent background
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp // Slight shadow for a "card" effect
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp), // Increased padding inside the card for a more spacious look
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Box
                Box(
                    modifier = Modifier
                        .size(80.dp) // Increased logo size to match the second screenshot
                        .background(Color.Black.copy(alpha = 0.4f)) // Slightly transparent box
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = channelLogoUrl,
                        contentDescription = "Channel Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp)) // Increased space between logo and text for better balance

                // Channel Details
                Column {
                    Text(
                        text = channelNumber,
                        color = Color.White,
                        fontSize = 24.sp, // Increased font size to match the second screenshot
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = channelName,
                        color = Color.White,
                        fontSize = 20.sp, // Increased font size to match the second screenshot
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Auto-hide after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000)
        onTimeout()
    }
}

