package com.berbas.heraconnectcommon.localData.person

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The database class for room
 */
@Database(
    entities = [Person::class],
    version = 7
)
abstract class PersonDataBase : RoomDatabase() {

    abstract val dao: PersonDao
}
