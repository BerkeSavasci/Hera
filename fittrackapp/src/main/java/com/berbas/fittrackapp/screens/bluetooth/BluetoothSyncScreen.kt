package com.berbas.fittrackapp.screens.bluetooth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.navigation.BottomBarScreens


/**
 * Represents the UI elements of the bluetooth screen
 */
@Composable
fun BluetoothSyncScreen(
    bluetoothViewModel: BluetoothSyncViewModel,
    navController: NavHostController
) {
    val dataTransferStatus by bluetoothViewModel.dataTransferStatus.collectAsState()

    BackHandler {
        bluetoothViewModel.release()
        navController.popBackStack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth") },
                contentColor = Color.White
            )
        },
        floatingActionButton = { BluetoothFloatingActionButton(bluetoothViewModel, navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BluetoothButtons(bluetoothViewModel)
            BluetoothDeviceList(bluetoothViewModel)
        }
    }
    /**
     * observe the status and show the appropriate UI screen to the user
     * TODO: change some stuff
     */
    when (dataTransferStatus) {
        BluetoothSyncViewModel.DataTransferStatus.STARTED -> {
            Toast
                .makeText(
                    LocalContext.current,
                    "The data transfer has started!",
                    Toast.LENGTH_SHORT
                )
                .show()
        }

        BluetoothSyncViewModel.DataTransferStatus.IN_PROGRESS -> {
            Toast
                .makeText(
                    LocalContext.current,
                    "The data transfer is in progress please don't close the window!",
                    Toast.LENGTH_SHORT
                )
                .show()
        }

        BluetoothSyncViewModel.DataTransferStatus.SUCCESS -> {
            Toast
                .makeText(
                    LocalContext.current,
                    "The data transfer was successful you can go back to the profile view!",
                    Toast.LENGTH_SHORT
                )
                .show()
        }

        BluetoothSyncViewModel.DataTransferStatus.FAILURE -> {
            Toast
                .makeText(
                    LocalContext.current,
                    "The data transfer was unsuccessful, please try again later!",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}

/**
 * The button to navigate back to the profile screen
 */
@Composable
fun BluetoothFloatingActionButton(
    bluetoothViewModel: BluetoothSyncViewModel,
    navController: NavHostController
) {
    FloatingActionButton(onClick = {
        bluetoothViewModel.stopBluetoothServer()
        bluetoothViewModel.release()
        navController.navigate(BottomBarScreens.Profile.route)
    }) {
        Text("Done")
    }
}

/**
 * The button layouts for the start and stop scanning
 */
@Composable
fun BluetoothButtons(bluetoothViewModel: BluetoothSyncViewModel) {
    Row(modifier = Modifier.padding(8.dp)) {
        Button(onClick = { bluetoothViewModel.startDiscovery() }) {
            Text("Scan")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { bluetoothViewModel.stopDiscovery() }) {
            Text("Stop Scanning")
        }
    }
    Divider()
}

/**
 * A UI to display the scanned devices as a list of items
 */
@Composable
fun BluetoothDeviceList(bluetoothViewModel: BluetoothSyncViewModel) {
    val devices = bluetoothViewModel.devices.collectAsState().value
    val context = LocalContext.current

    Text(
        text = "List of scanned devices:",
        fontSize = 20.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(devices.filter { it.name != null }) { device ->
            Card(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(
                        onClick = {
                            Toast
                                .makeText(
                                    context,
                                    "Device Clicked: ${device.name}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            bluetoothViewModel.connectToDevice(device)
                        }
                    ),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Bluetooth Icon",
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = device.name.toString(),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}