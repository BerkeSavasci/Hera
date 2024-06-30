package com.berbas.hera.presentation.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.rememberPickerState

@Composable
fun GoalsScreen(
    onBackClick: () -> Unit,
    viewModel: GoalsViewModel
) {
    val state by viewModel.state.collectAsState()
    var showStepGoalDialog by remember { mutableStateOf(false) }
    var showActivityGoalDialog by remember { mutableStateOf(false) }


    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        item {
            PickerItem(
                label = "Step Goal",
                value = "${state.stepGoal}",
                onClick = {
                    showStepGoalDialog = true
                }
            )
        }
        item {
            PickerItem(
                label = "Activity Goal",
                value = "${state.activityGoal}",
                onClick = {
                    showActivityGoalDialog = true
                }
            )
        }
    }
    if (showActivityGoalDialog) {
        ActivityGoalPicker(
            selectedActivityGoal = state.weight,
            onActivityGoalSelected = { newValue ->
                viewModel.onEvent(PersonEvent.SetActivityGoal(newValue))
                showActivityGoalDialog = false
            }
        )
    }
    if (showStepGoalDialog) {
        StepGoalDialog(
            currentStepGoal = state.stepGoal,
            onStepGoalUpdated = { newGoal ->
                viewModel.onEvent(PersonEvent.SetStepGoal(newGoal))
                showStepGoalDialog = false
            },
            onDismissRequest = { showStepGoalDialog = false }
        )
    }
}

/**
 * A pop up Alert dialog (a new screen in wear os) to update the weight
 */
@Composable
fun ActivityGoalPicker(
    selectedActivityGoal: Double,
    onActivityGoalSelected: (Double) -> Unit
) {
    val intPart = selectedActivityGoal.toInt()
    val fractionPart = ((selectedActivityGoal - intPart) * 10).toInt()

    val intPickerState = rememberPickerState(
        initialNumberOfOptions = 24,
        initiallySelectedOption = intPart,
        repeatItems = false
    )

    val fracPickerState = rememberPickerState(
        initialNumberOfOptions = 9,
        initiallySelectedOption = fractionPart,
        repeatItems = true
    )

    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = { /* Handle dismiss */ },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(30, 30, 30)
                ),
                onClick = {
                    onActivityGoalSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
                }) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Select new goal",
                color = Color.White,
                style = MaterialTheme.typography.title2,
            )
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Picker(
                    state = intPickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Weight Integer Part Picker",
                    onSelected = {
                        onActivityGoalSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
                    }
                ) {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = ".",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Picker(
                    state = fracPickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Weight Fractional Part Picker",
                    onSelected = {
                        onActivityGoalSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
                    }
                ) {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}

@Composable
fun StepGoalDialog(
    currentStepGoal: Int,
    onStepGoalUpdated: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    var stepGoal by remember { mutableStateOf(currentStepGoal) }

    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(30, 30, 30)
                ),
                onClick = { onStepGoalUpdated(stepGoal) },
            ) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Set Step Goal",
                color = Color.White,
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (stepGoal >= 500) stepGoal -= 500
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background
                        ),
                        modifier = Modifier
                    ) {
                        Text(text = "-500", color = Color.White)
                    }
                    Text(
                        text = "$stepGoal",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    )
                    Button(
                        onClick = { stepGoal += 500 },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background
                        ),

                    ) {
                        Text(text = "+500", color = Color.White)
                    }
                }
            }
        }
    )
}
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewStepGoalDialog() {
    StepGoalDialog(
        currentStepGoal = 6000,
        onStepGoalUpdated = {},
        onDismissRequest = {}
    )
}