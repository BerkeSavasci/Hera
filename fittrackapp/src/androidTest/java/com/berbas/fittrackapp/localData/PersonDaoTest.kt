package com.berbas.fittrackapp.localData

import androidx.test.filters.SmallTest
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.person.PersonDataBase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class PersonDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_PersonDB")
    lateinit var personDataBase: PersonDataBase

    private lateinit var dao: PersonDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = personDataBase.dao
    }

    @After
    fun tearDown() {
        personDataBase.close()
    }

    @Test
    fun insertPersonEntity() = runTest {
        val dummyPerson = Person(
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0,
            id = 101
        )
        dao.upsertPerson(dummyPerson)

        val retrievedPerson = dao.getPersonById(dummyPerson.id).first()
        assertEquals(dummyPerson, retrievedPerson)
    }
    @Test
    fun deletePersonEntity() = runTest {
        val dummyPerson = Person(
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0,
            id = 101
        )
        dao.upsertPerson(dummyPerson)
        dao.deletePerson(dummyPerson)

        val retrievedPerson = dao.getPersonById(dummyPerson.id).firstOrNull()
        assertNull(retrievedPerson)
    }

    @Test
    fun updatePersonEntity() = runTest {
        val dummyPerson = Person(
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0,
            id = 101
        )
        dao.upsertPerson(dummyPerson)

        val updatedPerson = dummyPerson.copy(weight = 85.0)
        dao.upsertPerson(updatedPerson)

        val retrievedPerson = dao.getPersonById(dummyPerson.id).first()
        assertEquals(updatedPerson, retrievedPerson)
    }

    @Test
    fun getPersonEntityByName() = runTest {
        val dummyPerson = Person(
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0,
            id = 101
        )
        dao.upsertPerson(dummyPerson)

        val retrievedPerson = dao.getPersonByName(dummyPerson.firstname, dummyPerson.lastname).first()
        assertEquals(dummyPerson, retrievedPerson)
    }

    @Test
    fun getPersonEntityById() = runTest {
        val dummyPerson = Person(
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0,
            id = 101
        )
        dao.upsertPerson(dummyPerson)

        val retrievedPerson = dao.getPersonById(dummyPerson.id).first()
        assertEquals(dummyPerson, retrievedPerson)
    }
}