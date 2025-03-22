package com.aktechhub.livetvapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aktechhub.livetvapp.R
import com.aktechhub.livetvapp.navigation.Destinations
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    val isTv = LocalContext.current.resources.configuration.uiMode and
            Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION

    // Menu Items with Icons
    val menuItems = listOf(
        MenuItem("Live TV", R.drawable.main_tv, Destinations.LIVE_TV),
        MenuItem("Movies", R.drawable.main_vod, ""),
        MenuItem("Web Series", R.drawable.main_vod, ""),
        MenuItem("Radio", R.drawable.main_radio, ""),
        MenuItem("Music", R.drawable.main_radio, ""),
        MenuItem("Settings", R.drawable.main_settings, "")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_home),  // Replace with your background
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar: Logo & Date-Time
            TopBar()

            Spacer(modifier = Modifier.height(32.dp))

            // Horizontal Scrollable Menu
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(menuItems.size) { index ->
                    MenuItemCard(menuItems[index], isTv) {
                        if (menuItems[index].route.isNotEmpty()) {
                            navController.navigate(menuItems[index].route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo
            contentDescription = "App Logo",
            modifier = Modifier.height(40.dp)
        )

        // Date-Time Display
        Text(
            text = getCurrentDateTime(),
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.alpha(0.8f)
        )
    }
}

// Card Item for Menu
@Composable
fun MenuItemCard(item: MenuItem, isTv: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(if (isTv) 150.dp else 120.dp)
            .focusable()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                color = Color.White,
                fontSize = if (isTv) 20.sp else 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Menu Item Data Class
data class MenuItem(val title: String, val icon: Int, val route: String)

// Get Current Date-Time
fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy\nHH:mm", Locale.getDefault())
    return sdf.format(Date())
}
