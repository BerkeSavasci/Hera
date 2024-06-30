package com.berbas.fittrackapp

import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** A fake person repository to test the [ProfileViewModel] */
class FakePersonRepository : PersonDao {

    // In-memory list of persons
    private val persons = mutableListOf<Person>()

    override suspend fun upsertPerson(person: Person) {
        persons.add(person)
    }

    override suspend fun deletePerson(person: Person) {
        persons.remove(person)
    }

    override fun getPersonById(id: Int): Flow<Person> {
        return flow {
            val person = persons.find { it.id == id }
            if (person != null) {
                emit(person)
            }
        }
    }

    override fun getPersonByName(firstname: String, lastname: String): Flow<Person> {
        return flow {
            val person = persons.find { it.firstname == firstname && it.lastname == lastname }
            if (person != null) {
                emit(person)
            }
        }
    }
}