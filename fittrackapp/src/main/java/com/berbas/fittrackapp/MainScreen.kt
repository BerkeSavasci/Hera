package com.berbas.fittrackapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.berbas.fittrackapp.navigation.AppScreens
import com.berbas.fittrackapp.navigation.BottomBarScreens
import com.berbas.fittrackapp.navigation.BottomNavGraph
import com.berbas.fittrackapp.screens.profile.ProfileViewModel

/**
 * A composable function that defines the navigation graph for the bottom navigation bar.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: ProfileViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Check if the current destination is not the Bluetooth screen
            if (currentDestination?.route != AppScreens.Bluetooth.route) {
                BottomBar(navController = navController)
            }
        }
    ) {
        BottomNavGraph(navController = navController, viewModel = viewModel)
    }
}

/**
 * Calls the predefined BottomNavigation for each screen. Then adds one item which is defined in
 * [AddItem] composable function.
 */
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreens.Goals,
        BottomBarScreens.Home,
        BottomBarScreens.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

/**
 * Calls the BottomNavigationItem for each screen to define properties.
 * See [BottomNavigationItem] for more information.
 */
@Composable
fun RowScope.AddItem(
    screen: BottomBarScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(
                text = screen.title
            )
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                // With this configuration, the back stack is popped up to the start destination of the graph.
                // Meaning the screens wont stack on top each other infinitely.
                popUpTo(navController.graph.findStartDestination().id) {
                    this@navigate.launchSingleTop = true
                }
            }
        }
    )
}
