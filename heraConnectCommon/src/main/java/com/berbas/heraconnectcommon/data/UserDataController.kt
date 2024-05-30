package com.berbas.heraconnectcommon.data

import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  USerDataController class is responsible for fetching and sending user data such as Settings User
 *  specific information, goals.
 *  Additionally, it is responsible for processing the data such as clean up duplicates.
 *
 */
class UserDataController(private val dao: PersonDao, private val id: Int) : DataController {
    override fun fetch(fetchedData: Any): Any {
        TODO("Not yet implemented")
    }

    override fun send(sendData: Any, connection: Any) {
        TODO("Not yet implemented")
    }

    /**
     * Insert a person into the database if they don't exist
     * update if they exist
     */
    suspend fun upsertPerson(person: Person) {
        withContext(Dispatchers.IO) {
            dao.upsertPerson(person)
        }
    }

    /**
     * get a person from the database by their id
     */
    suspend fun getPersonById(id: Int): Person {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id)
        }
    }

    /**
     * get a person from the database by their id and return their height
     */
    suspend fun getPersonHeightById(id: Int): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).height
        }
    }

    /**
     * get a person from the database by their id and return their weight
     */
    suspend fun getPersonWeightById(id: Int): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).weight
        }
    }

    /**
     * get a person from the database by their id and return their birthday
     */
    suspend fun getPersonBirthdayById(id: Int): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).birthday
        }
    }

    /**
     * get a person from the database by their id and return their gender
     */
    suspend fun getPersonGenderById(id: Int): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).gender
        }
    }
}