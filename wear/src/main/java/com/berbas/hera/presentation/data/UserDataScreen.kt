package com.berbas.hera.presentation.data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerDefaults
import androidx.wear.compose.material.rememberPickerState

/**
 * The profile screen where the information about the user can be found and edited
 */
@Composable
fun UserDataScreen(
    onBackClick: () -> Unit,
    viewModel: UserDataViewModel
) {
    MaterialTheme {
        val state by viewModel.state.collectAsState()
        var showHeightDialog by remember { mutableStateOf(false) }
        var showGenderDialog by remember { mutableStateOf(false) }
        var showWeightDialog by remember { mutableStateOf(false) }
        var showDateDialog by remember { mutableStateOf(false) }

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
                    label = "Gender",
                    value = state.gender,
                    onClick = {
                        showGenderDialog = true
                    }
                )
            }
            item {
                PickerItem(
                    label = "Birthday",
                    value = state.birthday,
                    onClick = {
                        showDateDialog = true
                    }
                )
            }
            item {
                PickerItem(
                    label = "Height",
                    value = "${state.height} cm",
                    onClick = {
                        showHeightDialog = true
                    }
                )
            }
            item {
                PickerItem(
                    label = "Weight",
                    value = "${state.weight} kg",
                    onClick = {
                        showWeightDialog = true
                    }
                )
            }
            // Add other items (BirthdayPicker) here
        }
        if (showHeightDialog) {
            HeightPickerDialog(
                selectedHeight = state.height,
                onHeightSelected = { newValue ->
                    viewModel.onEvent(PersonEvent.SetHeight(newValue))
                    showHeightDialog = false
                }
            )
        }
        if (showGenderDialog) {
            GenderPickerDialog(
                selectedGender = state.gender,
                onGenderSelected = { newValue ->
                    viewModel.onEvent(PersonEvent.SetGender(newValue))
                    showGenderDialog = false
                }
            )
        }
        if (showDateDialog) {
            DatePickerDialog(
                selectedDate = state.birthday,
                onDateSelected = { newValue ->
                    viewModel.onEvent(PersonEvent.SetBirthday(newValue))
                    showDateDialog = false
                }
            )
        }
        if (showWeightDialog) {
            WeightPickerDialog(
                selectedWeight = state.weight,
                onWeightSelected = { newValue ->
                    viewModel.onEvent(PersonEvent.SetWeight(newValue))
                    showWeightDialog = false
                }
            )
        }
    }
}

/**
 * The items of the profile screen
 */
@Composable
fun PickerItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color(30, 30, 30),
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = Color.Cyan
        )
    }
}

/** preview of the single items */
@Preview(showBackground = true)
@Composable
fun PreviewPickerItem() {
    MaterialTheme {
        PickerItem(
            label = "Preview Label",
            value = "Preview Value",
            onClick = { /* Do nothing in preview */ }
        )
    }
}

/**
 * A pop up Alert dialog (a new screen in wear os) for the gender selection
 */
@Composable
fun GenderPickerDialog(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val genderOptions = listOf("Male", "Female", "Other")
    val selectedIndex = genderOptions.indexOf(selectedGender).takeIf { it >= 0 } ?: 0
    val pickerState = rememberPickerState(
        initialNumberOfOptions = genderOptions.count(),
        initiallySelectedOption = selectedIndex,
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
                    onGenderSelected(genderOptions[pickerState.selectedOption])
                }) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Select Gender",
                color = Color.White,
                style = MaterialTheme.typography.title2,
            )
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Picker(
                    state = pickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Gender Picker",
                    onSelected = {
                        onGenderSelected(genderOptions[pickerState.selectedOption])
                    },
                    scalingParams = PickerDefaults.defaultScalingParams(),
                ) {
                    Text(
                        text = genderOptions[it],
                        style = MaterialTheme.typography.title3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}


@Preview(showBackground = true)
@Composable
fun GenderPickerDialogPreview() {
    GenderPickerDialog(selectedGender = "male") {
    }
}

/**
 * A pop up Alert dialog (a new screen in wear os) that allows the user to change their height
 */
@Composable
fun HeightPickerDialog(
    selectedHeight: Int,
    onHeightSelected: (Int) -> Unit
) {
    val heightRange = 50..250
    val selectedIndex = heightRange.indexOf(selectedHeight).takeIf { it >= 0 } ?: 0
    val pickerState = rememberPickerState(
        initialNumberOfOptions = heightRange.count(),
        initiallySelectedOption = selectedIndex,
        repeatItems = false
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
                    onHeightSelected(pickerState.selectedOption + heightRange.first)
                }) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Select Height",
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
                    state = pickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Height Picker",
                    onSelected = {
                        onHeightSelected(pickerState.selectedOption + heightRange.first)
                    },
                ) {
                    Text(
                        text = (it + heightRange.first).toString(),
                        style = MaterialTheme.typography.title3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}

/**
 * A pop up Alert dialog (a new screen in wear os) to set the users birthday
 */
@Composable
fun DatePickerDialog(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val dateParts = selectedDate.split("/")
    val selectedDay = dateParts[0].toInt()
    val selectedMonth = dateParts[1].toInt()
    val selectedYear = dateParts[2].toInt()

    val dayPickerState = rememberPickerState(
        initialNumberOfOptions = 31,
        initiallySelectedOption = selectedDay - 1,
        repeatItems = true
    )

    val monthPickerState = rememberPickerState(
        initialNumberOfOptions = 12,
        initiallySelectedOption = selectedMonth - 1,
        repeatItems = true
    )

    val yearPickerState = rememberPickerState(
        initialNumberOfOptions = 100,
        initiallySelectedOption = selectedYear - 1922,
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
                    onDateSelected("${dayPickerState.selectedOption + 1}/${monthPickerState.selectedOption + 1}/${yearPickerState.selectedOption + 1922}")
                }) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Select Date",
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
                    state = dayPickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Day Picker",
                    onSelected = {
                        onDateSelected("${dayPickerState.selectedOption + 1}/${monthPickerState.selectedOption + 1}/${yearPickerState.selectedOption + 1922}")
                    }
                ) {
                    Text(
                        text = (it + 1).toString(),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "/",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Picker(
                    state = monthPickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Month Picker",
                    onSelected = {
                        onDateSelected("${dayPickerState.selectedOption + 1}/${monthPickerState.selectedOption + 1}/${yearPickerState.selectedOption + 1922}")
                    }
                ) {
                    Text(
                        text = (it + 1).toString(),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "/",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Picker(
                    state = yearPickerState,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Year Picker",
                    onSelected = {
                        onDateSelected("${dayPickerState.selectedOption + 1}/${monthPickerState.selectedOption + 1}/${yearPickerState.selectedOption + 1922}")
                    }
                ) {
                    Text(
                        text = (it + 1922).toString(),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}

/**
 * A pop up Alert dialog (a new screen in wear os) to update the weight
 */
@Composable
fun WeightPickerDialog(
    selectedWeight: Double,
    onWeightSelected: (Double) -> Unit
) {
    // TODO: make the background color match the rest of the app
    // FIXME: make the ui more appealing, change the scroll mechanics

    val intPart = selectedWeight.toInt()
    val fractionPart = ((selectedWeight - intPart) * 10).toInt()

    val intPickerState = rememberPickerState(
        initialNumberOfOptions = 300,
        initiallySelectedOption = intPart,
        repeatItems = false
    )

    val fracPickerState = rememberPickerState(
        initialNumberOfOptions = 10,
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
                    onWeightSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
                }) {
                Text(text = "OK", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Select Weight",
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
                        onWeightSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
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
                        onWeightSelected(intPickerState.selectedOption + fracPickerState.selectedOption / 10.0)
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
