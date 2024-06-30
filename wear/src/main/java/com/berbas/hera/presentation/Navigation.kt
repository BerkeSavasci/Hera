package com.berbas.hera.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.berbas.hera.presentation.data.UserDataScreen
import com.berbas.hera.presentation.data.UserDataViewModel
import com.berbas.hera.presentation.sync.SyncSelectionScreen
import com.berbas.hera.presentation.sync.bluetooth.SyncScreen
import com.berbas.hera.presentation.sync.bluetooth.BluetoothSyncViewModel
import com.berbas.hera.presentation.sync.wifi.WifiSyncScreen
import com.berbas.hera.presentation.sync.wifi.WifiSyncViewModel

@Composable
fun FitnessApp(
    bluetoothSyncViewModel: BluetoothSyncViewModel,
    wifiSyncViewModel: WifiSyncViewModel,
    userDataViewModel: UserDataViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "overview") {
        composable("overview") {
            FitnessOverviewScreen(
                steps = 6315,
                heartRate = 75,
                sleep = "7h 35min",
                onStepsClick = { navController.navigate("steps_detail") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("steps_detail") {
            StepsDetailScreen(
                steps = 6315,
                goal = 7000,
                distance = 4.92,
                calories = 300,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.navigateUp() },
                navController = navController
            )
        }
        composable("update_data")
        {
            UserDataScreen(
                onBackClick = { navController.navigateUp() },
                viewModel = userDataViewModel
            )
        }
        composable("sync_selection")
        {
            SyncSelectionScreen(
                navController = navController
            )
        }
        composable("bluetooth_sync")
        {
            SyncScreen(
                viewModel = bluetoothSyncViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable("wifi_sync")
        {
            WifiSyncScreen(
                viewModel = wifiSyncViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
