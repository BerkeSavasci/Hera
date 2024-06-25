package com.berbas.fittrackapp.localData

import androidx.test.filters.SmallTest
import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDao
import com.berbas.heraconnectcommon.localData.PersonDataBase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
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
    @Named("test_DB")
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
}