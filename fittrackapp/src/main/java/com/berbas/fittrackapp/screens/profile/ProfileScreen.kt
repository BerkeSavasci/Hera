package com.berbas.fittrackapp.screens.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.berbas.fittrackapp.screens.bluetooth.BluetoothSyncViewModel
import com.berbas.fittrackapp.ui.theme.HeraTheme
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
    HeraTheme {
        val state = profileViewModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    actions = {
                        IconButton(onClick = {
                            // when opening the screen start the server automatically
                            bluetoothViewModel.startBluetoothServer()
                            navController.navigate(Screen.BLUETOOTHSCREEN.name)
                        }) {
                            Icon(painterResource(id = R.drawable.sync_icon), contentDescription = "Sync")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.onBackground)
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
}

/**
 * About you text
 */
@Composable
fun AboutYouSection() {
    Text(
        text = "About you",
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(vertical = 8.dp)
        // TODO: add a divider here
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
        color = Color.White,
        modifier = Modifier.padding(vertical = 8.dp)
        // TODO: add a divider here
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
            Text(text = "Step Goal", color = Color.White)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF1F1F1F))
                    .clickable { showStepGoalDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$stepGoal steps", color = Color.White)
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
            Text(text = "Active Time Goal (hours)", color = Color.White)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF1F1F1F))
                    .clickable { showActiveTimeGoalDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$activeTimeGoal h", color = Color.White)
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
            Text(text = "Gender", color = Color.White)
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
            Text(text = "Birthday", color = Color.White)
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
            Text(text = "Weight (kg)", color = Color.White)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF1F1F1F))
                    .clickable { showWeightDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$weight kg", color = Color.White)
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
            Text(text = "Height (cm)", color = Color.White)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF1F1F1F))
                    .clickable { showHeightDialog = true }
                    .padding(16.dp)
            ) {
                Text(text = "$height cm", color = Color.White)
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
            .background(Color(0xFF1F1F1F))
            .clickable { expanded = true }
            .padding(16.dp)
    ) {
        Text(text = selectedGender, color = Color.White)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { gender ->
                DropdownMenuItem(onClick = {
                    onGenderSelected(gender)
                    expanded = false
                }) {
                    Text(text = gender)
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
            .background(Color(0xFF1F1F1F))
            .clickable { showDialog = true }
            .padding(16.dp)
    ) {
        Text(text = birthday, color = Color.White)
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
                label = { Text(label, color = Color.White) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    textColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onValueChange(
                    if (isInteger) inputValue.toDoubleOrNull()?.toInt()?.toDouble()
                        ?: value else inputValue.toDoubleOrNull() ?: value
                )
                onDismissRequest()
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text("Cancel")
            }
        },
        backgroundColor = Color(0xFF1F1F1F),
        contentColor = Color.White
    )
}


