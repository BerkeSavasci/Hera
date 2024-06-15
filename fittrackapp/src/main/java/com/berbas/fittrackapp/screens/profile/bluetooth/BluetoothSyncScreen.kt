package com.berbas.fittrackapp.screens.profile.bluetooth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.navigation.BottomBarScreens

data class BluetoothDevice(val name: String)

@Composable
fun BluetoothSyncScreen(bluetoothViewModel: BluetoothSyncViewModel, navController: NavHostController) {
    val devices = bluetoothViewModel.devices.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth") },
                contentColor = Color.White
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                bluetoothViewModel.release()
                navController.navigate(BottomBarScreens.Profile.route)
            }) {
                Text("Done")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "ON/OFF indicator for Bluetooth",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider()

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
            Text(
                text = "List of scanned devices:",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(devices) { device ->
                    Text(device.name.toString(), modifier = Modifier.clickable(
                        onClick = {
                            bluetoothViewModel.connectToDevice(device)
                        }
                    ))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBluetoothSyncScreen() {

}