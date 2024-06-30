package com.berbas.fittrackapp.screens.connections.bluetooth

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.R
import com.berbas.fittrackapp.navigation.BottomBarScreens
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothConnection

@Composable
fun BluetoothSyncScreen(
    bluetoothViewModel: BluetoothSyncViewModel,
    navController: NavHostController
) {
    val dataTransferStatus by bluetoothViewModel.dataTransferStatus.collectAsState()
    val showDialog = remember { mutableStateOf(true) }

    BackHandler {
        bluetoothViewModel.release()
        navController.popBackStack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth", color = MaterialTheme.colorScheme.onSecondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        },
        floatingActionButton = {
            BluetoothFloatingActionButton(bluetoothViewModel, navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            BluetoothButtons(bluetoothViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            BluetoothDeviceList(bluetoothViewModel)
        }
    }
    if (showDialog.value) {
        DataTransferDialog(dataTransferStatus, showDialog)
    }
}

@Composable
fun DataTransferDialog(
    dataTransferStatus: BluetoothConnection.DataTransferStatus,
    showDialog: MutableState<Boolean>
) {
    when (dataTransferStatus) {
        BluetoothConnection.DataTransferStatus.IN_PROGRESS -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Data Transfer") },
                text = {
                    Column {
                        Text("Data transfer is in progress, please don't close the window!")
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                },
                confirmButton = {TextButton(onClick = { showDialog.value = false }) {
                    Text("OK")
                } }
            )
        }

        BluetoothConnection.DataTransferStatus.SUCCESS -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Data Transfer") },
                text = { Text("The data transfer was successful! You can go back to the profile view.") },
                confirmButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("OK")
                    }
                }
            )
        }

        BluetoothConnection.DataTransferStatus.FAILURE -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Data Transfer") },
                text = {
                    Text(
                        "The data transfer was unsuccessful! Please check your connectivity" +
                                "and try again."
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("OK")
                    }
                }
            )
        }

        BluetoothConnection.DataTransferStatus.IDLE -> {
            Log.d("SyncScreen", "Status is in IDLE")
        }

        BluetoothConnection.DataTransferStatus.STARTED -> {
            Log.d("SyncScreen", "Data transfer has started")
        }
    }
}

@Composable
fun BluetoothFloatingActionButton(
    bluetoothViewModel: BluetoothSyncViewModel,
    navController: NavHostController
) {
    FloatingActionButton(
        onClick = {
            bluetoothViewModel.stopBluetoothServer()
            bluetoothViewModel.release()
            navController.navigate(BottomBarScreens.Profile.route)
        },
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Text("Done")
    }
}

@Composable
fun BluetoothButtons(bluetoothViewModel: BluetoothSyncViewModel) {
    val isScanning = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.padding(8.dp)) {
            Button(
                onClick = {
                    if (isScanning.value) {
                        bluetoothViewModel.stopDiscovery()
                    } else {
                        bluetoothViewModel.startDiscovery()
                    }
                    isScanning.value = !isScanning.value
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Scan Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(if (isScanning.value) "Stop" else "Scan")
            }
            if (isScanning.value) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 8.dp),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

@Composable
fun BluetoothDeviceList(bluetoothViewModel: BluetoothSyncViewModel) {
    val devices = bluetoothViewModel.devices.collectAsState().value
    val context = LocalContext.current

    Text(
        text = "List of scanned devices:",
        fontSize = 20.sp,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(devices.filter { it.name != null }) { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(
                        onClick = {
                            Toast
                                .makeText(
                                    context,
                                    "Connecting to ${device.name}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            bluetoothViewModel.connectToDevice(device)
                        }
                    ),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bluetooth_icon),
                        contentDescription = "Bluetooth Icon",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = device.name.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}
