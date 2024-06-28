package com.berbas.heraconnectcommon.localData.sensor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FitnessData")
data class FitnessData(
    var steps: ArrayList<String>,
    var bpm: ArrayList<String>,
    var sleepTime: ArrayList<String>,
    var initialStepCount: Int = 0,
    var cumulativeSteps: Int = 0,
    @PrimaryKey
    val id: Int = 1
)