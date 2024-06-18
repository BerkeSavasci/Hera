package com.berbas.fittrackapp.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import com.berbas.fittrackapp.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

/**
 * Sealed class representing the screens of the bottom bar.
 */
sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home : BottomBarScreens(
        route = "home",
        title = "Home",
        icon = R.drawable.home_icon
    )

    object Profile : BottomBarScreens(
        route = "profile",
        title = "Profile",
        icon = R.drawable.person_icon
    )

    object Goals : BottomBarScreens(
        route = "goals",
        title = "Goals",
        icon = R.drawable.flag_icon
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
sealed class AppScreens(val route: String) {
    object Bluetooth : AppScreens(Screen.BLUETOOTHSCREEN.name)
}