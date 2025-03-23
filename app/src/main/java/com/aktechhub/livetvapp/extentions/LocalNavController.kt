package com.aktechhub.livetvapp.extentions

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController


val LocalNavController = staticCompositionLocalOf<NavHostController> { error("Please provide NavHostController") }