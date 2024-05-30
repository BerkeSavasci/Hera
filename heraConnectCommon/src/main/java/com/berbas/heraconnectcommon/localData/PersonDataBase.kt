package com.berbas.heraconnectcommon.localData

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The database class for room
 */
@Database(
    entities = [Person::class],
    version = 1
)
abstract class PersonDataBase : RoomDatabase() {

    abstract val dao: PersonDao
}