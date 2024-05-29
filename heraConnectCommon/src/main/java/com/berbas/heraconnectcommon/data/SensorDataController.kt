package com.berbas.heraconnectcommon.data

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

/**
 * It is responsible for retrieving sensor data from the device.
 * After processing the data, it sends it to the FitnessDataController.
 */
class SensorDataController(
    private val context: Context,
    private val fitnessDataController: FitnessDataController
) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    private val permissions = setOf(
        Record.createReadPermission(StepsRecord::class),
        Record.createReadPermission(HeartRateRecord::class),
        Record.createReadPermission(SleepSessionRecord::class)
    )

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun readStepData(startTime: Instant, endTime: Instant) {
        coroutineScope.launch {
            if (checkPermissions()) {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                fitnessDataController.onStepDataReceived(response.records)
            } else {
                // Handle the case where permissions are not granted
                // This could involve requesting permissions again or informing the user
                // For simplicity, we're just logging a message here.
                Log.d("SensorDataController", "Permissions not granted for step data.")
            }
        }
    }

    fun readPulseData(startTime: Instant, endTime: Instant) {
        coroutineScope.launch {
            if (checkPermissions()) {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                fitnessDataController.onPulseDataReceived(response.records)
            } else {
                Log.d("SensorDataController", "Permissions not granted for heart rate data.")
            }
        }
    }

    fun readSleepData(startTime: Instant, endTime: Instant) {
        coroutineScope.launch {
            if (checkPermissions()) {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        SleepSessionRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                fitnessDataController.onSleepDataReceived(response.records)
            } else {
                Log.d("SensorDataController", "Permissions not granted for sleep data.")
            }
        }
    }

    // Helper function to check if permissions are granted
    private fun checkPermissions(): Boolean {
        val grantedPermissions = healthConnectClient.permissionController
            .getGrantedPermissions(permissions)
        return grantedPermissions == permissions
    }
}