package com.aktechhub.livetvapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aktechhub.livetvapp.navigation.Destinations

@Composable
fun HomeScreen(navController: NavController) {
    val isTv = LocalContext.current.resources.configuration.uiMode and
            Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION
    val menuItems = listOf("Live TV", "Movies", "Web Series", "Radio", "Music", "Settings")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "Welcome",
            color = Color.White,
            fontSize = if (isTv) 36.sp else 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isTv) 3 else 2),
            modifier = Modifier.padding(16.dp)
        ) {
            items(menuItems) { item ->
                MenuItemCard(item, isTv) {
                    when (item) {
                        "Live TV" -> navController.navigate(Destinations.LIVE_TV)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(title: String, isTv: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .focusable()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isTv) 150.dp else 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = if (isTv) 24.sp else 18.sp
            )
        }
    }
}
