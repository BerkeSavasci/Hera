package com.berbas.heraconnectcommon.localData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PersonDao {

    /**
     * Insert a person into the database
     * if the person with the id already exists, update the fields with the new values
     */
    @Upsert
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
    fun getPersonById(id: Int): Person

    /**
     * Get a person from the database by their name
     */
    @Query("SELECT * FROM Person WHERE firstname = :firstname AND lastname = :lastname")
    fun getPersonByName(firstname: String, lastname: String): Person


}