package com.berbas.fittrackapp.localData

import androidx.test.filters.SmallTest
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessDatabase
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
class FitnessDaoTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_FitnessDB")
    lateinit var fitnessDatabase: FitnessDatabase

    private lateinit var dao: FitnessDataDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = fitnessDatabase.dao
    }

    @After
    fun tearDown() {
        fitnessDatabase.close()
    }

    @Test
    fun insertSensorDataEntity() = runTest {
        val dummyFitnessData = FitnessData(
            steps = arrayListOf("1000", "2000", "3000"),
            bpm = arrayListOf("70", "80", "90"),
            sleepTime = arrayListOf("7", "8", "9"),
            id = 101
        )
        dao.insertSensorData(dummyFitnessData)

        val retrievedFitnessData = dao.getSensorData().first()

        assertEquals(dummyFitnessData.id, retrievedFitnessData.id)
    }

    @Test
    fun deleteSensorDataEntity() = runTest {
        val dummyFitnessData = FitnessData(
            steps = arrayListOf("1000", "2000", "3000"),
            bpm = arrayListOf("70", "80", "90"),
            sleepTime = arrayListOf("7", "8", "9"),
            id = 101
        )
        dao.insertSensorData(dummyFitnessData)
        dao.deleteSensorData(dummyFitnessData)

        val retrievedFitnessData = dao.getSensorData().firstOrNull()
        assertNull(retrievedFitnessData)
    }
}