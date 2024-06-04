package com.berbas.heraconnectcommon.data

import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

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
    suspend fun getPersonById(id: Int): Person? {
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

    /**
     * Set the sleep time for the user.
     * This could be used to track the user's sleep schedule.
     *
     * @param toBed The time the user wants to go to bed.
     * @param wakeUp The time the user wants to wake up.
     */
    fun setStepGoal(stepGoal: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Set the step goal for the user.
     * This could be used to track the user's daily step count goal.
     *
     * @param stepGoal The step goal to set for the user.
     */
    fun setSleepTime(toBed: LocalTime, wakeUp: LocalTime) {
        TODO("Not yet implemented")
    }

    /**
     * Set the gender for the user.
     * This could be used for personalizing the user's profile.
     *
     * @param gender The gender of the user.
     */
    suspend fun setGender(gender: String) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id)
            person.gender = gender
            dao.upsertPerson(person)
        }
    }

    /**
     * Set the birth date for the user.
     * This could be used for calculating the user's age.
     *
     * @param birthDate The birth date of the user.
     */
    suspend fun setBirthDate(birthDate: Date) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id)
            val dateFormat = SimpleDateFormat("dd MM yy", Locale.getDefault())
            person.birthday = dateFormat.format(birthDate) // Assuming the date is stored as a string
            dao.upsertPerson(person)
        }
    }

    suspend fun setWeight(weight: Double) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id)
            person.weight = weight.toString() // Assuming the weight is stored as a string
            dao.upsertPerson(person)
        }
    }

    suspend fun setHeight(height: Double) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id)
            person.height = height.toString() // Assuming the height is stored as a string
            dao.upsertPerson(person)
        }
    }

    /**
     * View the data of the user.
     * This could be used to display all the user's profile information.
     */
    fun viewData() {

    }
}