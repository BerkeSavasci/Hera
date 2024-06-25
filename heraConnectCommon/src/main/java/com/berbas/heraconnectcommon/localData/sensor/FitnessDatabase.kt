package com.berbas.heraconnectcommon.localData.sensor

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FitnessData::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class  FitnessDatabase : RoomDatabase() {
    abstract val dao: FitnessDataDao
}