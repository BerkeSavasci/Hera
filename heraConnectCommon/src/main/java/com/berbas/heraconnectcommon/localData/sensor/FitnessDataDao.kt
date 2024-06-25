package com.berbas.heraconnectcommon.localData.sensor

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSensorData(fitnessData: FitnessData)

    @Delete
    suspend fun deleteSensorData(fitnessData: FitnessData)

    @Query("SELECT * FROM FitnessData")
    fun getSensorData(): Flow<FitnessData>
}