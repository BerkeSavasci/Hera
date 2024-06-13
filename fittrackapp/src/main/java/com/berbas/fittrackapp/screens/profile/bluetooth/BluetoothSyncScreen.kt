package com.berbas.fittrackapp.screens.profile.bluetooth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.navigation.BottomBarScreens

data class BluetoothDevice(val name: String)

@Composable
fun BluetoothSyncScreen(devices: List<BluetoothDevice>, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth") },
                contentColor = Color.White
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
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
            Text(
                text = "List of scanned devices:",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
            items(devices) { device ->
                Text(device.name, modifier = Modifier.clickable(
                    onClick = {
                        // TODO: handle device click
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