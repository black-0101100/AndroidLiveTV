package com.aktechhub.livetvapp.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aktechhub.livetvapp.R
import com.aktechhub.livetvapp.navigation.Destinations

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler { showExitDialog = true }

    var selectedIndex by remember { mutableIntStateOf(0) } // Tracks focused item

    val menuItems = listOf(
        MenuItem("TV", R.drawable.main_tv, Color(0xFFE91E63)),
        MenuItem("Movies", R.drawable.main_vod, Color(0xFF8BC34A)),
        MenuItem("Radio", R.drawable.main_radio, Color(0xFF2196F3)),
        MenuItem("Settings", R.drawable.main_settings, Color(0xFFFF9800)),
        MenuItem("Music", R.drawable.main_radio, Color(0xFF4CAF50)),
        MenuItem("Apps", R.drawable.main_settings, Color(0xFF03A9F4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onKeyEvent { event ->  // D-pad key navigation
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionRight -> {
                            if (selectedIndex < menuItems.lastIndex) selectedIndex++
                            true
                        }
                        Key.DirectionLeft -> {
                            if (selectedIndex > 0) selectedIndex--
                            true
                        }
                        Key.Enter, Key.NumPadEnter -> { // Handle selection
                            navigateToScreen(navController, menuItems[selectedIndex].title)
                            true
                        }
                        else -> false
                    }
                } else false
            }
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "SkyPlay Logo",
            modifier = Modifier
                .padding(24.dp)
                .size(120.dp)
        )

        // Date & Time
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp, top = 16.dp)
        ) {
            Text(
                text = "Saturday, 22 March 2025",
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "16:26",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Bottom Navigation Menu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            menuItems.forEachIndexed { index, item ->
                MenuItemCard(
                    item = item,
                    isSelected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        navigateToScreen(navController, item.title)
                    }
                )
            }
        }
    }

    // Exit Dialog
    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = { activity?.finish() },
            onDismiss = { showExitDialog = false }
        )
    }
}

// Function to navigate based on menu item selection
private fun navigateToScreen(navController: NavController, title: String) {
    when (title) {
        "TV" -> navController.navigate(Destinations.LIVE_TV)
        "Movies" -> navController.navigate(Destinations.LIVE_TV)
        "Radio" -> navController.navigate(Destinations.LIVE_TV)
        "Settings" -> navController.navigate(Destinations.LIVE_TV)
    }
}

@Composable
fun MenuItemCard(item: MenuItem, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1.0f, label = "")

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .width(140.dp * scale)
            .height(80.dp * scale)
            .focusable(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) item.color.copy(alpha = 0.8f) else item.color.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 16.dp else 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.2f),
                            item.color.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.size(if (isSelected) 48.dp else 32.dp) // Bigger when selected
            )
            Text(
                text = item.title,
                color = Color.White,
                fontSize = if (isSelected) 18.sp else 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class MenuItem(val title: String, val iconRes: Int, val color: Color)

@Composable
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Exit App", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Are you sure you want to exit?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", fontSize = 18.sp, color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", fontSize = 18.sp, color = Color.Blue)
            }
        }
    )
}
