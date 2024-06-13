package com.berbas.heraconnectcommon.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

/*
/**
 *  UserDataController class is responsible for fetching and sending user data such as Settings User
 *  specific information, goals.
 *  Additionally, it is responsible for processing the data such as clean up duplicates.
 *
 */
class UserDataController(private val dao: PersonDao, private val id: Int) : DataController,
    ProfileActionsManagerInterface {

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
    suspend fun getPerson(): Flow<Person> {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id)
        }
    }

    /**
     * get a person from the database by their id and return their height
     */
    suspend fun getPersonHeight(): Double {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).first().height
        }
    }

    /**
     * get a person from the database by their id and return their weight
     */
    suspend fun getPersonWeight(): Double {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).first().weight
        }
    }

    /**
     * get a person from the database by their id and return their birthday
     */
    suspend fun getPersonBirthday(): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).first().birthday
        }
    }

    /**
     * get a person from the database by their id and return their gender
     */
    suspend fun getPersonGender(): String {
        return withContext(Dispatchers.IO) {
            dao.getPersonById(id).first().gender
        }
    }

    /**
     * Set the sleep time for the user.
     * This could be used to track the user's sleep schedule.
     *
     * @param toBed The time the user wants to go to bed.
     * @param wakeUp The time the user wants to wake up.
     */
    override fun setStepGoal(stepGoal: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Set the step goal for the user.
     * This could be used to track the user's daily step count goal.
     *
     * @param stepGoal The step goal to set for the user.
     */
    override fun setSleepTime(toBed: LocalTime, wakeUp: LocalTime) {
        TODO("Not yet implemented")
    }

    /**
     * Set the gender for the user.
     * This could be used for personalizing the user's profile.
     *
     * @param gender The gender of the user.
     */
    override suspend fun setGender(gender: String) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id).first()
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
    override suspend fun setBirthDate(birthDate: Date) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id).first()
            val dateFormat = SimpleDateFormat("dd MM yy", Locale.getDefault())
            person.birthday =
                dateFormat.format(birthDate) // Assuming the date is stored as a string
            dao.upsertPerson(person)
        }
    }

    override suspend fun setWeight(weight: Double) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id).first()
            person.weight = weight
            dao.upsertPerson(person)
        }
    }

    override suspend fun setHeight(height: Double) {
        withContext(Dispatchers.IO) {
            val person = dao.getPersonById(id).first()
            person.height = height
            dao.upsertPerson(person)
        }
    }
}

 */