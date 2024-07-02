package com.berbas.fittrackapp.screens.connections.wifi

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class WifiSyncScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSendButtonTriggersViewModel() {
        val fakeViewModel = FakeWifiSyncViewModel()
        composeTestRule.setContent {
            val navController =rememberNavController()
            WifiSyncScreen(wifiSyncViewModel = fakeViewModel, navController = navController)
        }

        // Find the "Send" button and click it
        composeTestRule.onNodeWithText("Send").performClick()

        // Assert that the sendData function in the ViewModel was called
        assert(fakeViewModel.sendDataCalled)
    }

    @Test
    fun testReceiveButtonTriggersViewModel() {
        val fakeViewModel = FakeWifiSyncViewModel()
        composeTestRule.setContent {
            val navController = rememberNavController()
            WifiSyncScreen(wifiSyncViewModel = fakeViewModel, navController = navController)
        }

        // Find the "Receive" button and click it
        composeTestRule.onNodeWithText("Receive").performClick()

        // Assert that the receiveData function in the ViewModel was called
        assert(fakeViewModel.receiveDataCalled)
    }

    @Test
    fun testTitleIsDisplayed() {
        val fakeViewModel = FakeWifiSyncViewModel()
        composeTestRule.setContent {
            val navController = rememberNavController()
            WifiSyncScreen(wifiSyncViewModel = fakeViewModel, navController= navController)
        }

        composeTestRule.onNodeWithText("Wifi").assertIsDisplayed()
    }
}