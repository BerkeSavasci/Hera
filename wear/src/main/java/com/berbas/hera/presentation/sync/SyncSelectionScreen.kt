package com.berbas.hera.presentation.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.berbas.hera.presentation.sync.bluetooth.BluetoothSyncViewModel

@Composable
fun SyncSelectionScreen(
    navController: NavHostController,
    bluetoothSyncViewModel: BluetoothSyncViewModel
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Back to settings
            IconButton(onClick = { navController.navigate("settings") }) {

            }
        }
        item {
            // Bluetooth sync
            SyncItem(
                "Bluetooth",
                onClick = {
                    bluetoothSyncViewModel.startBluetoothServer()
                    navController.navigate("bluetooth_sync")
                }
            )
        }
        item {
            // Wifi sync
            SyncItem(
                "Wifi",
                onClick = {
                    navController.navigate("wifi_sync")
                }
            )
        }
    }
}

@Composable
fun SyncItem(
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color(30, 30, 30),
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}