package com.berbas.fittrackapp.screens.connections.wifi

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.R
import com.berbas.fittrackapp.navigation.BottomBarScreens
import com.berbas.heraconnectcommon.connection.wifi.WifiState

@Composable
fun WifiSyncScreen(
    wifiSyncViewModel: WifiSyncViewModel,
    navController: NavHostController,
) {
    val wifiConnectionImage: Painter = painterResource(id = R.drawable.data_transfer_cloud)

    val wifiState = wifiSyncViewModel.wifiState.collectAsState().value
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wifi", color = MaterialTheme.colorScheme.onSecondary) },
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
            FloatingActionButton(
                onClick = { navController.navigate(BottomBarScreens.Profile.route) },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Text("Done")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select a sync method",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Image(
                painter = wifiConnectionImage,
                contentDescription = "Connecting Devices",
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    wifiSyncViewModel.sendData()
                    Toast.makeText(
                        context,
                        "Sending data to the server...",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ios_share),
                    contentDescription = "Send Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
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
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.call_received),
                    contentDescription = "Receive Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Receive")
            }
            DataTransferDialog(
                wifiState,
                context
            )
        }
    }
}

@Composable
fun DataTransferDialog(
    dataTransferStatus: WifiState,
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

        WifiState.SERVER_ERROR -> {
            Log.d("WifiSyncScreen", "Status: ${dataTransferStatus.name}")
            Toast.makeText(
                context,
                "Cannot connect to the server",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    backgroundColor: Color
) {
    FloatingActionButton(onClick = onClick, containerColor = backgroundColor) {
        Text("Done")
    }
}
