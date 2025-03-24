package com.aktechhub.livetvapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
                .fillMaxWidth(fraction = 0.95f) // Take 95% of the screen width to match the image
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp) // Padding around the card
                .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)), // White border
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
                    .padding(16.dp), // Padding inside the card
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Logo (No box, larger size, transparent background)
                AsyncImage(
                    model = channelLogoUrl,
                    contentDescription = "Channel Logo",
                    modifier = Modifier
                        .size(80.dp) // Larger size for the logo
                        .background(Color.Transparent) // Transparent background
                )

                Spacer(modifier = Modifier.width(16.dp)) // Space between logo and details

                // Channel Details (Right Section)
                Row(
                    modifier = Modifier
                        .weight(1f), // Take remaining space
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f) // Take most of the space, leaving room for the star icon
                    ) {
                        // Channel Number and Name in the same row
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$channelNumber - ",
                                color = Color.White, // White text for channel number
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = channelName,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Horizontal Line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.White.copy(alpha = 0.5f)) // White line with slight transparency
                                .padding(top = 8.dp)
                        )

                        // More space for future EPG data
                        Spacer(modifier = Modifier.height(48.dp)) // Increased space for EPG
                    }

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