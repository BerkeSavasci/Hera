package com.berbas.fittrackapp.ui.interactionTests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.berbas.fittrackapp.screens.connections.wifi.WifiSyncScreen
import com.berbas.fittrackapp.screens.connections.wifi.WifiSyncViewModel
import org.junit.Rule
import org.junit.Test

class WifiSyncScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun wifiSyncScreen_buttonsFunctionProperly() {
        val wifiSyncViewModel = WifiSyncViewModel() // provide necessary params

        composeTestRule.setContent {
            val navController = rememberNavController()
            WifiSyncScreen(wifiSyncViewModel, navController)
        }

        composeTestRule.onNodeWithText("Send").performClick()
        composeTestRule.onNodeWithText("Receiving data from the server...").assertIsDisplayed()
    }
}