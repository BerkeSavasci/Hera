package com.berbas.fittrackapp.screens.connections

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

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