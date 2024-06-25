package com.berbas.heraconnectcommon.localData.person

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for the person table
 */
@Dao
interface PersonDao {

    /**
     * Insert a person into the database
     * if the person with the id already exists, update the fields with the new values
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPerson(person: Person)

    /**
     * Delete a person from the database
     */
    @Delete
    suspend fun deletePerson(person: Person)

    /**
     * Get a person from the database by their id
     */
    @Query("SELECT * FROM Person WHERE id = :id")
    fun getPersonById(id: Int): Flow<Person>

    /**
     * Get a person from the database by their name
     */
    @Query("SELECT * FROM Person WHERE firstname = :firstname AND lastname = :lastname")
    fun getPersonByName(firstname: String, lastname: String): Flow<Person>


}

