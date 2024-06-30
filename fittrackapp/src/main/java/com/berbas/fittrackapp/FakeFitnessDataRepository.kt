package com.berbas.fittrackapp

import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** A fake fitness data repository to test the [ProfileViewModel] */
class FakeFitnessDataRepository : FitnessDataDao {

    // In-memory list of fitness data
    private val fitnessDataList = mutableListOf<FitnessData>()

    override suspend fun insertSensorData(fitnessData: FitnessData) {
        fitnessDataList.add(fitnessData)
    }

    override suspend fun deleteSensorData(fitnessData: FitnessData) {
        fitnessDataList.remove(fitnessData)
    }

    override fun getSensorData(): Flow<FitnessData> {
        return flow {
            val fitnessData = fitnessDataList.firstOrNull()
            if (fitnessData != null) {
                emit(fitnessData)
            }
        }
    }
}