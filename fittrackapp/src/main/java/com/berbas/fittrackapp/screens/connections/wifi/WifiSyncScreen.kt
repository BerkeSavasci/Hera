package com.berbas.fittrackapp.screens.connections.wifi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WifiSyncScreen(
    wifiSyncViewModel: WifiSyncViewModel,
    navController: NavHostController
) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Wifi") },
                contentColor = Color.White
            )
        }

    ) { paddingValues ->
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
                onClick = { wifiSyncViewModel.sendData() },
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Send")
            }
            Button(
                onClick = { wifiSyncViewModel.receiveData() },
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Receive")
            }
        }
    }

}

/**
 * Represents the UI elements of the wifi screen
 */
@Composable
fun SyncButton(syncFunction: () -> Unit) {
    Button(
        onClick = { syncFunction() }) {
        Text("receive")
    }
}

@Composable
fun SendButton(syncFunction: () -> Unit) {
    Button(
        onClick = { syncFunction() }) {
        Text("send")
    }
}



