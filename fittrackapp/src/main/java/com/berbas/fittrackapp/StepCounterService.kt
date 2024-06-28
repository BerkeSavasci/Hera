package com.berbas.fittrackapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : LifecycleService(), SensorEventListener {

    private var totalSteps = 0f
    private var initialStepCount = -1
    private var cumulativeSteps = 0

    private lateinit var sensorManager: SensorManager

    companion object {
        const val CHANNEL_ID = "com.berbas.fittrackapp.step_counter_channel"
        const val CHANNEL_NAME = "Step Counter Service Channel"
        const val NOTIFICATION_ID = 1
    }

    @Inject
    lateinit var fitnessDataDao: FitnessDataDao

    override fun onCreate() {
        Log.d("StepCounterService", "stepping in")
        super.onCreate()

        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Step Counter Service")
            .setContentText("The service is running...")
            .setSmallIcon(R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        startForeground(NOTIFICATION_ID, notification)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.i("StepCounterService", "Step sensor registered")
        } else {
            Log.e("StepCounterService", "No step sensor found")
        }

        CoroutineScope(Dispatchers.IO).launch {
            val existingFitnessData = fitnessDataDao.getSensorData().firstOrNull()
            if (existingFitnessData != null) {
                initialStepCount = existingFitnessData.initialStepCount
                cumulativeSteps = existingFitnessData.cumulativeSteps
            } else {

                val initialFitnessData = FitnessData(
                    steps = arrayListOf(),
                    bpm = arrayListOf(),
                    sleepTime = arrayListOf(),
                    initialStepCount = 0,
                    cumulativeSteps = 0
                )
                fitnessDataDao.insertSensorData(initialFitnessData)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            totalSteps = event.values[0]

            if (initialStepCount == -1) {
                initialStepCount = totalSteps.toInt()
            }

            val currentSteps = totalSteps.toInt() - initialStepCount
            Log.i("Steptaken", "step taken: $currentSteps")
            saveStepsToDatabase(currentSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not needed
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun saveStepsToDatabase(steps: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingFitnessData = fitnessDataDao.getSensorData().firstOrNull()
            val today = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

            if (existingFitnessData != null) {
                val lastEntry = existingFitnessData.steps.lastOrNull()
                if (lastEntry != null && lastEntry.startsWith(today)) {
                    // Update today's step count
                    val parts = lastEntry.split(": ")
                    val updatedSteps = parts[1].toInt() + steps
                    existingFitnessData.steps[existingFitnessData.steps.size - 1] =
                        "$today: $updatedSteps"
                } else {
                    // Add new entry for today
                    existingFitnessData.steps.add("$today: $steps")
                }
                existingFitnessData.cumulativeSteps += steps
                fitnessDataDao.insertSensorData(existingFitnessData)
            } else {
                val fitnessData = FitnessData(
                    steps = arrayListOf("$today: $steps"),
                    bpm = arrayListOf(),
                    sleepTime = arrayListOf(),
                    cumulativeSteps = steps,
                    initialStepCount = totalSteps.toInt()
                )
                fitnessDataDao.insertSensorData(fitnessData)
            }

            // Update initialStepCount with totalSteps
            initialStepCount = totalSteps.toInt()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
