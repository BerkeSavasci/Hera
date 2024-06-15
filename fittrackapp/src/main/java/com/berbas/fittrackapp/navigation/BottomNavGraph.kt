package com.berbas.fittrackapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.berbas.fittrackapp.screens.goals.GoalsScreen
import com.berbas.fittrackapp.screens.home.HomeScreen
import com.berbas.fittrackapp.screens.profile.ProfileScreen
import com.berbas.fittrackapp.screens.profile.ProfileViewModel
import com.berbas.fittrackapp.screens.profile.bluetooth.BluetoothSyncScreen
import com.berbas.fittrackapp.screens.profile.bluetooth.BluetoothSyncViewModel

/**
 * A composable function that defines the navigation graph for the bottom navigation bar.
 * contains the screens of the bottom bar.
 * @param navController the navigation controller
 */
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    bluetoothViewModel: BluetoothSyncViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreens.Home.route
    ) {
        composable(route = BottomBarScreens.Goals.route) {
            GoalsScreen(navController)
        }
        composable(route = BottomBarScreens.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreens.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                bluetoothViewModel = bluetoothViewModel,
                navController = navController
            )
        }
        composable(route = AppScreens.Bluetooth.route) {
            BluetoothSyncScreen(
                bluetoothViewModel,
                navController
            )
        }

    }
}