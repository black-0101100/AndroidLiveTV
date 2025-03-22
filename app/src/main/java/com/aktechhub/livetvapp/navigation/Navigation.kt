package com.aktechhub.livetvapp.navigation

// Navigation.kt
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aktechhub.livetvapp.ui.HomeScreen
import com.aktechhub.livetvapp.ui.LiveTvScreen
import com.aktechhub.livetvapp.ui.SplashScreen

object Destinations {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val LIVE_TV = "liveTv"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.SPLASH) {
        composable(Destinations.SPLASH) {
            SplashScreen(navController)
        }
        composable(Destinations.HOME) {
            HomeScreen(navController)
        }
        composable(Destinations.LIVE_TV) {
            LiveTvScreen{
                navController.popBackStack() // Exit Live TV screen when back is pressed
            }
        }
    }
}

