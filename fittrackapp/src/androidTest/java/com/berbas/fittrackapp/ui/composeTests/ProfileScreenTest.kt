package com.berbas.fittrackapp.ui.composeTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import org.mockito.Mockito.mock
import com.berbas.fittrackapp.MainActivity
import com.berbas.fittrackapp.screens.connections.bluetooth.BluetoothSyncViewModel
import com.berbas.fittrackapp.screens.profile.ProfileScreen
import com.berbas.fittrackapp.screens.profile.ProfileViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProfileScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun profileScreen_displaysStepGoalAndActiveTimeGoalTexts() {
        // Mock your view models or use real instances if you prefer
        val mockProfileViewModel = mock(ProfileViewModel::class.java)
        val mockBluetoothViewModel = mock(BluetoothSyncViewModel::class.java)

        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = mockProfileViewModel,
                bluetoothViewModel = mockBluetoothViewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule.onNode(hasText("Step Goal")).assertIsDisplayed()
        composeTestRule.onNode(hasText("Active Time Goal")).assertIsDisplayed()
    }
}