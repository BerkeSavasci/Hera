package com.berbas.fittrackapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing the screens of the bottom bar.
 */
sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreens(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Profile : BottomBarScreens(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Goals : BottomBarScreens(
        route = "goals",
        title = "Goals",
        icon = Icons.Default.Star
    )
}

/**
 * Enum class representing the screens of the app excluding the bottom bar.
 */
enum class Screen {
    BLUETOOTHSCREEN
}

/**
 * Sealed class representing the screens of the app excluding the bottom bar.
 */
sealed class AppScreens(val route: String){
    object Bluetooth: AppScreens(Screen.BLUETOOTHSCREEN.name)
}