package com.berbas.fittrackapp.screens.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
                            Icon(Icons.Default.Build, contentDescription = "Sync")
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
    )
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
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

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
            { _, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                onBirthdaySelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
                showDialog = false
            },
            year,
            month,
            day
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
    var inputValue by remember { mutableStateOf(if (isInteger) value.toInt().toString() else String.format("%.1f", value)) }

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
                onValueChange(if (isInteger) inputValue.toDoubleOrNull()?.toInt()?.toDouble() ?: value else inputValue.toDoubleOrNull() ?: value)
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


