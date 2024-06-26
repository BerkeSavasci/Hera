package com.berbas.fittrackapp.screens.connections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.berbas.fittrackapp.R
import com.berbas.fittrackapp.navigation.Screen

@Composable
fun SyncSelectionScreen(navController: NavHostController) {
    val connectingDevicesImage: Painter = painterResource(id = R.drawable.data_transfer)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Syncing", color = MaterialTheme.colorScheme.onSecondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSecondary
                            )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        },
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
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = connectingDevicesImage,
                contentDescription = "Connecting Devices",
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate(Screen.BLUETOOTHSCREEN.name) },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bluetooth_icon),
                    contentDescription = "Bluetooth Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Bluetooth")
            }
            Button(
                onClick = { navController.navigate(Screen.WIFISCREEN.name) },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wifi_icon),
                    contentDescription = "Wifi Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Wifi")
            }
        }
    }
}

