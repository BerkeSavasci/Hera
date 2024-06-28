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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val stepCount by homeViewModel.stepCount.collectAsState()
    val stepGoal by homeViewModel.stepGoal.collectAsState()
    val lastSevenDaysSteps by homeViewModel.lastSevenDaysSteps.collectAsState()
    val isInfoDialogVisible by homeViewModel.isInfoDialogVisible

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
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
            StepProgressBar(stepCount = stepCount, stepGoal = stepGoal)
            Spacer(modifier = Modifier.size(20.dp))
            StepGoalText(stepGoal = stepGoal)
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

@Composable
fun StepProgressBar(stepCount: Int, stepGoal: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(150.dp)
    ) {
        CircularProgressIndicator(
            progress = stepCount.toFloat() / stepGoal.toFloat(),
            strokeWidth = 10.dp,
            modifier = Modifier.size(150.dp),
            color = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.onBackground
        )
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

@Composable
fun StepGoalText(stepGoal: Int) {
    Text(
        text = "Step Goal: $stepGoal",
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.headlineSmall
    )
}

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
                    CircularProgressIndicator(
                        progress = stepCount.toFloat() / stepGoal,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.onBackground
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
            val text = buildAnnotatedString {
                append("This is an early version of the Fitness track app \"HERA\". Some features might be partially implemented or not implemented.\nCreated by ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("\nSebastian Gey & Ilkkan Berke Savasci")
                }
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
