package com.aktechhub.livetvapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun ChannelDetailScreen(
    channelNumber: Int,
    channelName: String,
    channelLogoUrl: String, // URL of the logo image
    onTimeout: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000) // Hide after 5 seconds
        visible = false
        onTimeout()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)), // Semi-transparent background
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.DarkGray.copy(alpha = 0.9f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Logo (Load image using Coil)
                AsyncImage(
                    model = channelLogoUrl,
                    contentDescription = "Channel Logo",
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Channel Details
                Column {
                    Text(
                        text = "$channelNumber  $channelName",
                        color = Color.White,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}