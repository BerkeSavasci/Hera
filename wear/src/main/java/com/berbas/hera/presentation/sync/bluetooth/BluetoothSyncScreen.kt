package com.berbas.hera.presentation.sync.bluetooth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain


@Composable
fun BluetoothSyncScreen(
    viewModel: BluetoothSyncViewModel,
    onBackClick: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val isScanning = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.transferStatus.collect { statusMessage ->
            Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                IconButton(onClick = {
                    if (isScanning.value) { // if still scanning stop the scanning
                        viewModel.stopDiscovery()
                        isScanning.value = false
                    }
                    viewModel.stopBluetoothServer()
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            item {
                Button(onClick = {
                    if (isScanning.value) {
                        viewModel.stopDiscovery()
                    } else {
                        viewModel.startDiscovery()
                    }
                    isScanning.value = !isScanning.value
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isScanning.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        Text(if (isScanning.value) " Stop" else "Scan")
                    }
                }
            }
            itemsIndexed(devices) { index, device ->
                Device(
                    device = device,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun Device(
    device: BluetoothDeviceDomain,
    viewModel: BluetoothSyncViewModel
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                Toast
                    .makeText(
                        context,
                        "Device Clicked: ${device.name}",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                viewModel.connectToDevice(device)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = Color(30, 30, 30),
            contentColor = Color.White,
            disabledContainerColor = Color(40, 40, 40),
            disabledContentColor = Color.Gray
        )
    ) {
        Text(
            text = device.name ?: "Unknown device",
            modifier = Modifier.padding(16.dp)
        )
    }
}