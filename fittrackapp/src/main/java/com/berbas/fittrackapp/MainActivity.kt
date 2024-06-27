package com.berbas.fittrackapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berbas.fittrackapp.screens.profile.ProfileViewModel
import com.berbas.fittrackapp.screens.connections.bluetooth.BluetoothSyncViewModel
import com.berbas.fittrackapp.screens.connections.wifi.WifiSyncViewModel
import com.berbas.fittrackapp.screens.home.HomeViewModel
import com.berbas.fittrackapp.ui.theme.HeraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUEST_CODE_STEP_COUNTER_PERMISSIONS = 102

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
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    @RequiresApi(Build.VERSION_CODES.S)
    private val stepCounterPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allStepCounterPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                stepCounterPermissions,
                REQUEST_CODE_STEP_COUNTER_PERMISSIONS
            )
        } else {
            startStepCounterService()
        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS)
        }

        setContent {
            HeraTheme {
                val bluetoothSyncViewModel: BluetoothSyncViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val wifiSyncViewModel: WifiSyncViewModel = viewModel()

                MainScreen(
                    profileViewModel = profileViewModel,
                    bluetoothSyncViewModel = bluetoothSyncViewModel,
                    wifiSyncViewModel = wifiSyncViewModel,
                    homeViewModel = homeViewModel
                )
            }
        }

    }

    private fun startStepCounterService() {
        val intent = Intent(this, StepCounterService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun allStepCounterPermissionsGranted() = stepCounterPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}
