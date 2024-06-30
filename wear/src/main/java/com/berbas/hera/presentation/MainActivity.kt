package com.berbas.hera.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import com.berbas.hera.presentation.data.UserDataViewModel
import com.berbas.hera.presentation.sync.bluetooth.BluetoothSyncViewModel
import com.berbas.hera.presentation.sync.wifi.WifiSyncViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val REQUEST_BLUETOOTH_PERMISSIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            MaterialTheme {
                val userDataViewModel: UserDataViewModel = viewModel()
                val bluetoothSyncViewModel: BluetoothSyncViewModel = viewModel()
                val wifiSyncViewModel: WifiSyncViewModel = viewModel()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    FitnessApp(
                        userDataViewModel = userDataViewModel,
                        bluetoothSyncViewModel = bluetoothSyncViewModel,
                        wifiSyncViewModel = wifiSyncViewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestBluetoothPermissions()
    }

    private fun requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ), REQUEST_BLUETOOTH_PERMISSIONS
            )
        }
    }
}
