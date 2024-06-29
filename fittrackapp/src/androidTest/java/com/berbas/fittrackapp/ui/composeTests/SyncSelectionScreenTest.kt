package com.berbas.fittrackapp.ui.composeTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.berbas.fittrackapp.navigation.Screen
import com.berbas.fittrackapp.screens.connections.SyncSelectionScreen
import org.junit.Rule
import org.junit.Test

class SyncSelectionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

//    @Test
//    fun syncSelectionScreen_navigateToBluetoothScreen() {
//        val navController = TestNavHostController(composeTestRule.activityRule.scenario)
//
//        composeTestRule.setContent {
//            navController.setGraph(R.navigation.your_navigation_graph) // Setze dein Navigations-Graph
//            SyncSelectionScreen(navController = navController)
//        }
//
//        composeTestRule.onNodeWithText("Bluetooth").performClick()
//
//        // Prüfen, ob die Navigation die richtige Route hat
//        composeTestRule.runOnIdle {
//            assert(navController.currentDestination?.route == Screen.BLUETOOTHSCREEN.name)
//        }
//    }
//
//    @Test
//    fun syncSelectionScreen_navigateToWifiScreen() {
//        val navController = TestNavHostController(composeTestRule.activityRule.scenario)
//
//        composeTestRule.setContent {
//            navController.setGraph(R.navigation.your_navigation_graph) // Setze dein Navigations-Graph
//            SyncSelectionScreen(navController = navController)
//        }
//
//        composeTestRule.onNodeWithText("Wifi").performClick()
//
//        // Prüfen, ob die Navigation die richtige Route hat
//        composeTestRule.runOnIdle {
//            assert(navController.currentDestination?.route == Screen.WIFISCREEN.name)
//        }
//    }
}