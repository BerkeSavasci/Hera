package com.berbas.fittrackapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berbas.fittrackapp.screens.profile.ProfileViewModel
import com.berbas.fittrackapp.screens.bluetooth.BluetoothSyncViewModel
import com.berbas.fittrackapp.ui.theme.HeraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_PERMISSIONS = 101

    @RequiresApi(Build.VERSION_CODES.S)
    private val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        "android.permission.health.READ_HEART_RATE",
        "android.permission.health.WRITE_HEART_RATE",
        "android.permission.health.READ_STEPS",
        "android.permission.health.WRITE_STEPS",
        Manifest.permission.BODY_SENSORS
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS)
        }
        setContent {
            HeraTheme {
                val bluetoothSyncViewModel: BluetoothSyncViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()

                MainScreen(profileViewModel, bluetoothSyncViewModel)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}