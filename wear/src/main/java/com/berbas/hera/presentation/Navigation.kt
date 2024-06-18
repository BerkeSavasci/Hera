package com.berbas.hera.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun FitnessApp() {
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
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
