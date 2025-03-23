package com.aktechhub.livetvapp.ui
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aktechhub.livetvapp.extentions.LocalNavController
import com.aktechhub.livetvapp.navigation.ScreenRoutes
import kotlinx.coroutines.delay

// SplashScreen.kt
@Composable
fun SplashScreen() {

    val navController = LocalNavController.current

    val isTv = LocalContext.current.resources.configuration.uiMode and
            Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION

    LaunchedEffect(Unit) {
        delay(2000)
        navController.popBackStack()
        navController.navigate(ScreenRoutes.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Live TV App",
            color = Color.White,
            fontSize = if (isTv) 48.sp else 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}