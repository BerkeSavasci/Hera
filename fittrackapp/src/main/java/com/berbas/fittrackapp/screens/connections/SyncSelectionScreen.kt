package com.berbas.fittrackapp.screens.connections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.navigation.Screen

@Composable
fun SyncSelectionScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select a sync method",
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(Screen.BLUETOOTHSCREEN.name) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Bluetooth")
        }
        Button(
            onClick = { navController.navigate(Screen.WIFISCREEN.name) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Wifi")
        }
    }
}