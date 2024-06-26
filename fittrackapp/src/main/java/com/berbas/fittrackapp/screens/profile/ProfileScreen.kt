package com.berbas.fittrackapp.screens.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.R
import com.berbas.fittrackapp.navigation.Screen
import com.berbas.fittrackapp.screens.connections.bluetooth.BluetoothSyncViewModel
import java.util.*

/**
 * The view class for the profile screen
 */
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    bluetoothViewModel: BluetoothSyncViewModel,
    navController: NavHostController
) {
    val state = profileViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = {
                        // when opening the screen start the server automatically
                        bluetoothViewModel.startBluetoothServer()
                        navController.navigate(Screen.SELECTSCREEN.name)
                    }) {
                        Icon(
                            painterResource(id = R.drawable.sync_icon),
                            contentDescription = "Sync"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ActivitySection()
                StepAndActiveTimeGoalRow(
                    stepGoal = state.value.stepGoal,
                    onStepGoalChange = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetStepGoal(newValue)
                        )
                    },
                    activeTimeGoal = state.value.activityGoal,
                    onActiveTimeGoalChange = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetActivityGoal(newValue)
                        )
                    }
                )
                Divider(
                    color = Color.DarkGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(8.dp)
                )
                AboutYouSection()
                GenderAndBirthdayRow(
                    gender = state.value.gender,
                    onGenderSelected = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetGender(
                                newValue
                            )
                        )
                    },
                    birthday = state.value.birthday,
                    onBirthdaySelected = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetBirthday(
                                newValue
                            )
                        )
                    }
                )
                WeightAndHeightRow(
                    weight = state.value.weight,
                    onWeightChange = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetWeight(
                                newValue
                            )
                        )
                    },
                    height = state.value.height,
                    onHeightChange = { newValue ->
                        profileViewModel.onEvent(
                            PersonEvent.SetHeight(
                                newValue
                            )
                        )
                    }
                )
            }
        }
    }
}

/**
 * About you text
 */
@Composable
fun AboutYouSection() {
    Text(
        text = "About you",
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Activity goals text
 */
@Composable
fun ActivitySection() {
    Text(
        text = "Activity goals",
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * The UI elements for the step goal and active time goal fields
 */
@Composable
fun StepAndActiveTimeGoalRow(
    stepGoal: Int,
    onStepGoalChange: (Int) -> Unit,
    activeTimeGoal: Double,
    onActiveTimeGoalChange: (Double) -> Unit
) {
    var showStepGoalDialog by remember { mutableStateOf(false) }
    var showActiveTimeGoalDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Step Goal", color = MaterialTheme.colorScheme.onBackground)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable { showStepGoalDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$stepGoal steps", color = MaterialTheme.colorScheme.onSecondary)
            }
            if (showStepGoalDialog) {
                InputDialog(
                    label = "Step Goal",
                    value = stepGoal.toDouble(),
                    onValueChange = { newValue -> onStepGoalChange(newValue.toInt()) },
                    onDismissRequest = { showStepGoalDialog = false },
                    isInteger = true
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Active Time Goal (hours)", color = MaterialTheme.colorScheme.onBackground)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable { showActiveTimeGoalDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$activeTimeGoal h", color = MaterialTheme.colorScheme.onSecondary)
            }
            if (showActiveTimeGoalDialog) {
                InputDialog(
                    label = "Active Time Goal",
                    value = activeTimeGoal,
                    onValueChange = { newValue -> onActiveTimeGoalChange(newValue.toDouble()) },
                    onDismissRequest = { showActiveTimeGoalDialog = false },
                    isInteger = false
                )
            }
        }
    }
}

/**
 * The UI elements for the gender and birthday fields
 */
@Composable
fun GenderAndBirthdayRow(
    gender: String,
    onGenderSelected: (String) -> Unit,
    birthday: String,
    onBirthdaySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Gender", color = MaterialTheme.colorScheme.onBackground)
            GenderDropdown(
                selectedGender = gender,
                onGenderSelected = onGenderSelected
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Birthday", color = MaterialTheme.colorScheme.onBackground)
            BirthdayPicker(
                birthday = birthday,
                onBirthdaySelected = onBirthdaySelected
            )
        }
    }
}

/**
 * The UI elements for the weight and height row
 */
@Composable
fun WeightAndHeightRow(
    weight: Double,
    onWeightChange: (Double) -> Unit,
    height: Int,
    onHeightChange: (Int) -> Unit
) {
    var showWeightDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Weight (kg)", color = MaterialTheme.colorScheme.onBackground)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable { showWeightDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$weight kg", color = MaterialTheme.colorScheme.onSecondary)
            }
            if (showWeightDialog) {
                InputDialog(
                    label = "Weight",
                    value = weight,
                    onValueChange = onWeightChange,
                    onDismissRequest = { showWeightDialog = false }
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(text = "Height (cm)", color = MaterialTheme.colorScheme.onBackground)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable { showHeightDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$height cm", color = MaterialTheme.colorScheme.onSecondary)
            }
            if (showHeightDialog) {
                InputDialog(
                    label = "Height",
                    value = height.toDouble(),
                    onValueChange = { newValue -> onHeightChange(newValue.toInt()) },
                    onDismissRequest = { showHeightDialog = false },
                    isInteger = true
                )
            }
        }
    }
}

/**
 * A dropdown menu for the gender selection
 */
@Composable
fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Others")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { expanded = true }
            .padding(16.dp)
    ) {
        Text(text = selectedGender, color = MaterialTheme.colorScheme.onSecondary)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { gender ->
                DropdownMenuItem(onClick = {
                    onGenderSelected(gender)
                    expanded = false
                }) {
                    Text(text = gender, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

/**
 * A date picker to let the user select their birthday
 */
@Composable
fun BirthdayPicker(birthday: String, onBirthdaySelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Parse the selected date if possible, otherwise use the current date
    val selectedDate = if (birthday != "Placeholder" && birthday.split("/").size == 3) {
        birthday.split("/")
    } else {
        listOf(
            calendar.get(Calendar.DAY_OF_MONTH).toString(),
            (calendar.get(Calendar.MONTH) + 1).toString(),
            calendar.get(Calendar.YEAR).toString()
        )
    }

    val selectedDay = selectedDate[0].toInt()
    val selectedMonth = selectedDate[1].toInt() - 1 // Months are 0-indexed in Calendar
    val selectedYear = selectedDate[2].toInt()

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { showDialog = true }
            .padding(16.dp)
    ) {
        Text(text = birthday, color = MaterialTheme.colorScheme.onSecondary)
    }

    if (showDialog) {
        DatePickerDialog(
            context,
            { _, newYear: Int, newMonth: Int, newDay: Int ->
                onBirthdaySelected("$newDay/${newMonth + 1}/$newYear")
                showDialog = false
            },
            selectedYear,
            selectedMonth,
            selectedDay
        ).show()
    }
}

/**
 * The input dialog for both the weight and the height of the user
 */
@Composable
fun InputDialog(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    onDismissRequest: () -> Unit,
    isInteger: Boolean = false
) {
    var inputValue by remember {
        mutableStateOf(
            if (isInteger) value.toInt().toString() else String.format("%.1f", value)
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Enter your $label") },
        text = {
            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text(label, color = MaterialTheme.colorScheme.onSurface) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    textColor = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onValueChange(
                        if (isInteger) inputValue.toDoubleOrNull()?.toInt()?.toDouble()
                            ?: value else inputValue.toDoubleOrNull() ?: value
                    )
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Cancel")
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    )
}
