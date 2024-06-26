package com.berbas.fittrackapp

import android.Manifest
import android.content.Context
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
class MainActivity : ComponentActivity(), SensorEventListener {
    private val previousTotalSteps = 0f
    private var totalSteps: Float = 0f
    private var running: Boolean = false

    private var sensorManager: SensorManager? = null

    private lateinit var homeViewModel: HomeViewModel

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
                homeViewModel = viewModel()
                val wifiSyncViewModel: WifiSyncViewModel = viewModel()

                homeViewModel.stepCount.observe(this) { steps ->
                    Log.d("MainActivity", "Step count: $steps")
                }
                MainScreen(profileViewModel, bluetoothSyncViewModel, wifiSyncViewModel)
            }
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            if (::homeViewModel.isInitialized) {
                homeViewModel.stepCount.value = currentSteps
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // TODO
    }


    override fun onResume() {
        super.onResume()

        running = true
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        when {
            countSensor != null -> {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
                Log.i("Sensor", "Recognized step sensor")
            }

            else ->
                Toast.makeText(
                    this,
                    "Your device does not have a step sensor",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }

    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun allPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}