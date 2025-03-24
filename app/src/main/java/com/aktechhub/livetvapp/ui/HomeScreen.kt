package com.aktechhub.livetvapp.ui

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aktechhub.livetvapp.R
import com.aktechhub.livetvapp.extentions.LocalNavController
import com.aktechhub.livetvapp.navigation.ScreenRoutes
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Define menuItems outside the HomeScreen composable
private val menuItems = listOf(
    MenuItem("TV", R.drawable.main_tv, Color(0xFFE91E63)),
    MenuItem("VOD", R.drawable.main_vod, Color(0xFF8BC34A)),
    MenuItem("Radio", R.drawable.main_radio, Color(0xFF2196F3)),
    MenuItem("Settings", R.drawable.main_settings, Color(0xFFFF9800)),
    MenuItem("Profile", R.drawable.main_profile, Color(0xFF4CAF50)),
    MenuItem("Application", R.drawable.main_applications, Color(0xFF03A9F4))
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val activity = context as? Activity
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler { showExitDialog = true }

    // Default selected index is 0 (TV)
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Focus requester for each menu item
    val focusRequesters = remember { List(menuItems.size) { FocusRequester() } }

    // Current date and time in IST
    var currentDateTime by remember { mutableStateOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))) }
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
            kotlinx.coroutines.delay(1000L) // Update every second
        }
    }
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Request focus on the initially selected item (TV) with a small delay
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100L) // Small delay to ensure UI is ready
        focusRequesters[0].requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val logoSize = (screenWidth * 0.15f).coerceIn(80.dp, 150.dp) // Responsive logo size
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "SkyPlay Logo",
            modifier = Modifier
                .padding(start = 24.dp, top = 16.dp) // Moved up (reduced top padding)
                .size(logoSize)
        )

        // Date & Time
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp, top = 16.dp)
        ) {
            val textSize = if (screenWidth > 800.dp) 22.sp else 18.sp // Larger text for TV
            val clockSize = if (screenWidth > 800.dp) 56.sp else 48.sp
            Text(
                text = currentDateTime.format(dateFormatter),
                color = Color.White,
                fontSize = textSize
            )
            Text(
                text = currentDateTime.format(timeFormatter),
                color = Color.White,
                fontSize = clockSize,
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
                    focusRequester = focusRequesters[index],
                    onEnter = {
                        navigateToScreen(navController, item.title) // Handle Enter key or tap
                    },
                    onMoveLeft = {
                        if (selectedIndex > 0) {
                            selectedIndex--
                            focusRequesters[selectedIndex].requestFocus()
                        }
                    },
                    onMoveRight = {
                        if (selectedIndex < menuItems.lastIndex) {
                            selectedIndex++
                            focusRequesters[selectedIndex].requestFocus()
                        }
                    },
                    onFocusChange = { isFocused ->
                        if (isFocused) {
                            selectedIndex = index // Update selected index when focused
                        }
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
        "TV" -> navController.navigate(ScreenRoutes.LIVETV)
        "VOD" -> {} // Update with actual navigation if needed
        "Radio" -> {}
        "Settings" -> {}
        "Profile" -> {}
        "Application" -> {}
    }
}

@Composable
fun MenuItemCard(
    item: MenuItem,
    isSelected: Boolean,
    focusRequester: FocusRequester,
    onEnter: () -> Unit, // Callback for Enter key press or tap
    onMoveLeft: () -> Unit, // Callback for moving left
    onMoveRight: () -> Unit, // Callback for moving right
    onFocusChange: (Boolean) -> Unit // Callback for focus changes
) {
    var isFocused by remember { mutableStateOf(false) } // Track focus state for debugging

    val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1.0f, label = "")

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Adjusted card width to ensure all icons fit on the screen
    val cardWidth = when {
        screenWidth > 800.dp -> screenWidth * 0.12f  // Reduced size for Android TV
        else -> screenWidth * 0.15f                   // Reduced size for Mobile
    }
    val cardHeight = cardWidth * 0.6f  // Maintain aspect ratio

    // Dynamic icon size based on card width
    val baseIconSize = if (isSelected) (cardWidth * 0.3f).coerceIn(20.dp, 40.dp) // Reduced size when selected
    else (cardWidth * 0.5f).coerceIn(24.dp, 48.dp) // Reduced size when not selected
    val iconSize = baseIconSize

    Card(
        modifier = Modifier
            .padding(4.dp)
            .width((cardWidth * scale).coerceAtMost(screenWidth * 0.3f))
            .height(cardHeight * scale)
            .focusable()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChange(focusState.isFocused) // Update selected index when focused
            }
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    // Log the key code for debugging
                    Log.d("MenuItemCard", "Key pressed: ${event.key}")
                    when (event.key) {
                        Key.Enter, Key.NumPadEnter -> { // Added Key.Center for Android TV
                            onEnter() // Handle Enter key press
                            true
                        }
                        Key.DirectionLeft -> {
                            onMoveLeft() // Handle left navigation
                            true
                        }
                        Key.DirectionRight -> {
                            onMoveRight() // Handle right navigation
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .clickable { onEnter() } // Handle tap on mobile
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color.Yellow else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ), // Visual focus indicator
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) item.color.copy(alpha = 0.8f) else item.color.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 12.dp else 8.dp)
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
            verticalArrangement = if (isSelected) Arrangement.SpaceEvenly else Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit  // Ensure the icon fits within bounds
            )
            if (isSelected) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = (cardWidth * 0.08f).value.sp, // Dynamic text size
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class MenuItem(val title: String, val iconRes: Int, val color: Color)

@Composable
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dialogTextSize = if (screenWidth > 800.dp) 20.sp else 18.sp

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Exit App", fontSize = dialogTextSize, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Are you sure you want to exit?", fontSize = dialogTextSize)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", fontSize = dialogTextSize, color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", fontSize = dialogTextSize, color = Color.Blue)
            }
        }
    )
}