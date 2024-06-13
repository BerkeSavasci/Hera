package com.berbas.fittrackapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.berbas.fittrackapp.screens.goals.GoalsScreen
import com.berbas.fittrackapp.screens.home.HomeScreen
import com.berbas.fittrackapp.screens.profile.ProfileScreen
import com.berbas.fittrackapp.screens.profile.ProfileViewModel

/**
 * A composable function that defines the navigation graph for the bottom navigation bar.
 * contains the screens of the bottom bar.
 * @param navController the navigation controller
 */
@Composable
fun BottomNavGraph(navController: NavHostController, viewModel: ProfileViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Goals.route){
            GoalsScreen()
        }
        composable(route = BottomBarScreen.Home.route){
            HomeScreen()
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(viewModel =viewModel)
        }
    }
}