package com.berbas.fittrackapp.screens.home

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testInfoButtonOpensDialog() {
        // Set the content of the Compose rule
        composeTestRule.setContent {
            HomeScreen(homeViewModel = FakeHomeViewModel())
        }

        // Click on the Info button
        composeTestRule.onNodeWithContentDescription("Info").performClick()
        composeTestRule.onNode(hasText(
            "This is an early version of the Fitness track app \"HERA\". Some features might be partially implemented or not implemented. Created by Sebastian Gey & Ilkkan Berke Savasci"
        )).assertExists()
    }
    @Test
    fun testStepGoalDisplayed() {
        // Set the content of the Compose rule
        composeTestRule.setContent {
            HomeScreen(homeViewModel = FakeHomeViewModel())
        }

        // Verify that the step goal is displayed
        composeTestRule.onNode(hasText(
            ("Step Goal: 10000")
        )).assertExists()
    }

    @Test
    fun testStepCountDisplayed() {
        // Set the content of the Compose rule
        composeTestRule.setContent {
            HomeScreen(homeViewModel = FakeHomeViewModel())
        }

        // Verify that the step count title is displayed
        composeTestRule.onNodeWithText("Steps").assertExists()

        // Verify that the step count value is displayed
        composeTestRule.onNodeWithText("5500").assertExists()
    }

    @Test
    fun testLastSevenDaysStepsDisplayed() {
        // Set the content of the Compose rule
        composeTestRule.setContent {
            HomeScreen(homeViewModel = FakeHomeViewModel())
        }

        // Verify that the last seven days steps title is displayed
        composeTestRule.onNodeWithText("Last 7 Days Steps").assertExists()

        // Verify that the last seven days steps values are displayed
        listOf("5000", "7000", "8000", "6000", "9000", "10000", "11000").forEach { stepCount ->
            composeTestRule.onNodeWithText(stepCount).assertExists()
        }
    }
}
