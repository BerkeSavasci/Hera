package com.berbas.fittrackapp.screens.profile

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.berbas.fittrackapp.FakePersonRepository
import com.berbas.fittrackapp.navigation.Screen
import com.berbas.fittrackapp.screens.connections.SyncSelectionScreen
import com.berbas.fittrackapp.screens.connections.bluetooth.IBluetoothSyncViewModel
import com.berbas.fittrackapp.screens.connections.bluetooth.FakeBluetoothSyncViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var bluetoothSyncViewModel: IBluetoothSyncViewModel
    private lateinit var navController: TestNavHostController
    private lateinit var fakePersonRepository: FakePersonRepository
    private val testUserID = 1

    @Before
    fun setup() {
        bluetoothSyncViewModel = FakeBluetoothSyncViewModel()
        fakePersonRepository = FakePersonRepository()

        navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun profileScreenDisplaysInitialValues() {
        // Set the content of the Compose rule
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }

        // Verify that the initial values are displayed
        composeTestRule.onNodeWithText("male").assertExists()
        composeTestRule.onNodeWithText("01/01/2001").assertExists()
        composeTestRule.onNodeWithText("70.0 kg").assertExists()
        composeTestRule.onNodeWithText("175 cm").assertExists()
        composeTestRule.onNodeWithText("10000 steps").assertExists()
        composeTestRule.onNodeWithText("2.0 h").assertExists()
    }

    @Test
    fun testChangeStepGoal() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("10000 steps").performClick()
        composeTestRule.onNodeWithText("Enter your Step Goal").assertExists()


        composeTestRule.onNodeWithText("10000").performTextClearance()

        composeTestRule.onNodeWithText("").performTextInput("15000")
        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("15000 steps").assertExists()
    }

    @Test
    fun testChangeActivityTimeGoal() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("2.0 h").performClick()
        composeTestRule.onNodeWithText("Enter your Active Time Goal").assertExists()

        composeTestRule.onNodeWithText("2.0").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("3.5")
        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("3.5 h").assertExists()
    }

    @Test
    fun testChangeWeight() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("70.0 kg").performClick()
        composeTestRule.onNodeWithText("Enter your Weight").assertExists()

        composeTestRule.onNodeWithText("70.0").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("85.0")
        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("85.0 kg").assertExists()
    }

    @Test
    fun testChangeHeight() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("175 cm").performClick()
        composeTestRule.onNodeWithText("Enter your Height").assertExists()

        composeTestRule.onNodeWithText("175").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("165")
        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("165 cm").assertExists()
    }

    @Test
    fun testCancelChangeStepGoal() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("10000 steps").performClick()
        composeTestRule.onNodeWithText("Enter your Step Goal").assertExists()


        composeTestRule.onNodeWithText("10000").performTextClearance()

        composeTestRule.onNodeWithText("").performTextInput("15000")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("10000 steps").assertExists()
    }

    @Test
    fun testCancelChangeActivityTimeGoal() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("2.0 h").performClick()
        composeTestRule.onNodeWithText("Enter your Active Time Goal").assertExists()

        composeTestRule.onNodeWithText("2.0").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("3.5")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("2.0 h").assertExists()
    }

    @Test
    fun testCancelChangeWeight() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("70.0 kg").performClick()
        composeTestRule.onNodeWithText("Enter your Weight").assertExists()

        composeTestRule.onNodeWithText("70.0").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("85.0")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("70.0 kg").assertExists()
    }

    @Test
    fun testCancelChangeHeight() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("175 cm").performClick()
        composeTestRule.onNodeWithText("Enter your Height").assertExists()

        composeTestRule.onNodeWithText("175").performTextClearance()
        composeTestRule.onNodeWithText("").performTextInput("165")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("175 cm").assertExists()
    }

    @Test
    fun testChangeGender() {
        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = FakeProfileViewModel(fakePersonRepository, testUserID),
                bluetoothViewModel = bluetoothSyncViewModel,
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("male").performClick()

        composeTestRule.onNodeWithText("Female").performClick()

        composeTestRule.onNodeWithText("Female").assertExists()
    }
}
