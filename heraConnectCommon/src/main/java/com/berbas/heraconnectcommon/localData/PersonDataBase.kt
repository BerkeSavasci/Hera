package com.berbas.heraconnectcommon.localData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The database class for room
 */
@Database(
    entities = [Person::class],
    version = 4
)
abstract class PersonDataBase : RoomDatabase() {

    abstract val dao: PersonDao
}
