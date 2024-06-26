package com.berbas.fittrackapp.screens.connections.wifi

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.berbas.heraconnectcommon.connection.wifi.WifiState

@Composable
fun WifiSyncScreen(
    wifiSyncViewModel: WifiSyncViewModel,
    navController: NavHostController,
) {
    val wifiState = wifiSyncViewModel.wifiStatus

    val context = LocalContext.current
    Scaffold(
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
                onClick = {
                    wifiSyncViewModel.sendData()
                    Toast.makeText(
                        context,
                        "Sending data to the server...",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Send")
            }
            Button(
                onClick = {
                    wifiSyncViewModel.receiveData()
                    Toast.makeText(
                        context,
                        "Receiving data from the server...",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Receive")
            }
        }
    }
}

@Composable
fun DataTransferDialog(
    dataTransferStatus: WifiState,
    showDialog: MutableState<Boolean>,
    context: Context
) {
    when (dataTransferStatus) {
        WifiState.IDLE ->
            Log.d("WifiSyncScreen", "Switching back to IDLE state")

        WifiState.IN_PROGRESS ->
            Log.d("WifiSyncScreen", "Transfer in progress")

        WifiState.SUCCESS -> {
            Log.d("WifiSyncScreen", "Status: ${dataTransferStatus.name}")
            Toast.makeText(
                context,
                "Data transfer was successful",
                Toast.LENGTH_SHORT
            ).show()
        }
        WifiState.FAILURE -> {
            Log.d("WifiSyncScreen", "Status: ${dataTransferStatus.name}")
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
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



