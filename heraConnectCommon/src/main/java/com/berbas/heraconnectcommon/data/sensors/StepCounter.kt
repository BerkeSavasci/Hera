package com.berbas.heraconnectcommon.data.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class StepCounter(
    private val sensorManager: SensorManager
) {

    private val stepCounterSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private var stepCountListener: SensorEventListener? = null

    fun startTrackingSteps(listener: (Int) -> Unit) {
        stepCountListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.getOrNull(0)?.let { steps ->
                    listener(steps.toInt())
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }

        stepCounterSensor?.let { sensor ->
            sensorManager.registerListener(stepCountListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopTrackingSteps() {
        stepCountListener?.let { listener ->
            sensorManager.unregisterListener(listener)
        }
        stepCountListener = null
    }
}