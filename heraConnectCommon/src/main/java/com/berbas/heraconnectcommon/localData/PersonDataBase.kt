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
    version = 2
)
abstract class PersonDataBase : RoomDatabase() {

    abstract val dao: PersonDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: PersonDataBase? = null
    }
}