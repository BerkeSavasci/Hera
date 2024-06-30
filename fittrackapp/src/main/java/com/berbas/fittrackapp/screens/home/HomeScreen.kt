package com.berbas.fittrackapp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

/** Displays the contents of the HomeScreen */
@Composable
fun HomeScreen(homeViewModel: IHomeViewModel) {
    val stepCount by homeViewModel.stepCount.collectAsState()
    val stepGoal by homeViewModel.stepGoal.collectAsState()
    val lastSevenDaysSteps by homeViewModel.lastSevenDaysSteps.collectAsState()
    val isInfoDialogVisible by homeViewModel.isInfoDialogVisible

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home", color = MaterialTheme.colorScheme.onPrimary) },
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f))
                InfoButton(homeViewModel::showInfoDialog)
            }

            Spacer(modifier = Modifier.size(20.dp))
            StepProgressBar(
                stepCount = stepCount,
                stepGoal = stepGoal,
                size = 150,
                thickness = 10,
                showStepCount = true
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Step Goal: $stepGoal",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.size(20.dp))
            PlaceHolderRow()
            Spacer(modifier = Modifier.size(20.dp))
            LastSevenDaysStepsGraph(
                steps = lastSevenDaysSteps,
                stepGoal = stepGoal
            )
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchLastSevenDaysSteps()
    }

    if (isInfoDialogVisible) {
        InfoDialog(
            onDismiss = { homeViewModel.showInfoDialog(false) }
        )
    }
}

/**
 * A circular progress bar that is used to show the progress done by the user
 * [stepCount] the current step count of the user
 * [stepGoal] the current step goal of the user
 * [size] the size of the [CircularProgressIndicator]
 * [thickness] the thickness of the [CircularProgressIndicator]
 * [showStepCount] true if the step count needs to be shown false other wise
 * [goalReachedColor] the color of the [CircularProgressIndicator] when the step goal is reached,
 * has the default value {Color.Green.copy(alpha = 0.5f)}
 * [defaultColor] the color of the [CircularProgressIndicator] when the step goal hasn't been reached yet,
 * has the default value {MaterialTheme.colorScheme.primary}
 */
@Composable
fun StepProgressBar(
    stepCount: Int,
    stepGoal: Int,
    size: Int,
    thickness: Int,
    showStepCount: Boolean,
    goalReachedColor: Color = Color.Green.copy(alpha = 0.5f),
    defaultColor: Color = MaterialTheme.colorScheme.primary

) {
    var progression = 0f
    if (stepCount > 0) {
        progression = stepCount.toFloat() / stepGoal.toFloat()
    }
    val progressColor = if (stepCount >= stepGoal) {
        goalReachedColor
    } else {
        defaultColor
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size.dp)
    ) {
        CircularProgressIndicator(
            progress = progression,
            strokeWidth = thickness.dp,
            modifier = Modifier.size(size.dp),
            color = progressColor,
            backgroundColor = MaterialTheme.colorScheme.onBackground
        )
        if (showStepCount) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$stepCount",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Steps",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

/** Displays a graph allowing the user to see the last seven days of their step tracking data */
@Composable
fun LastSevenDaysStepsGraph(
    steps: List<Int>,
    stepGoal: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Last 7 Days Steps",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            steps.forEachIndexed { index, stepCount ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    StepProgressBar(
                        stepCount = stepCount,
                        stepGoal = stepGoal,
                        size = 40,
                        thickness = 7,
                        showStepCount = false
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stepCount.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

/** A placeholder row that displays fitness information */
@Composable
fun PlaceHolderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "--",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "cal",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "-- km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "distance",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "-- min",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "activity",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/** Info button used to show the info dialog*/
@Composable
fun InfoButton(onClick: (Boolean) -> Unit) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                onClick(true)
            }
        },
        content = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}

/** A UI element that informs the user about the latest status of the app */
@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "This is an early version of the Fitness track app \"HERA\". Some features might be partially implemented or not implemented. Created by Sebastian Gey & Ilkkan Berke Savasci",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
