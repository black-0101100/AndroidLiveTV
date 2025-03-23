package com.aktechhub.livetvapp.navigation

// Navigation.kt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aktechhub.livetvapp.extentions.LocalNavController
import com.aktechhub.livetvapp.ui.HomeScreen
import com.aktechhub.livetvapp.ui.LiveTvScreen
import com.aktechhub.livetvapp.ui.SplashScreen
import kotlinx.serialization.Serializable


sealed class ScreenRoutes {
    @Serializable
    data object SPLASH: ScreenRoutes()
    @Serializable
    data object HOME: ScreenRoutes()
    @Serializable
    data object LIVETV: ScreenRoutes()
}

@Composable
fun AppNavigation(navController: NavHostController) {

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {

        NavHost(navController = navController, startDestination = ScreenRoutes.SPLASH) {
            composable<ScreenRoutes.SPLASH> {
                SplashScreen()
            }
            composable<ScreenRoutes.HOME> {
                HomeScreen()
            }
            composable<ScreenRoutes.LIVETV> {
                LiveTvScreen {
                    navController.popBackStack() // Exit Live TV screen when back is pressed
                }
            }
        }

    }

}

