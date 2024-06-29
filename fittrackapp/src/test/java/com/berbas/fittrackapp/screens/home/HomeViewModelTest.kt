package com.berbas.fittrackapp.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val fitnessDataDao = mockk<FitnessDataDao>(relaxed = true)
    private val personDao = mockk<PersonDao>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(fitnessDataDao, personDao, 1)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchTodaySteps updates stepCount`() = runTest(testDispatcher) {
        val today = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        val fitnessData = FitnessData(
            steps = ArrayList(listOf("$today: 1000")),
            bpm = arrayListOf("80", "45"),
            sleepTime = arrayListOf("8", "5", "3.4"),
            initialStepCount = -1,
            cumulativeSteps = 0
        )

        coEvery { fitnessDataDao.getSensorData() } returns flow { emit(fitnessData) }

        viewModel.fetchTodaySteps()
        advanceUntilIdle()

        assertEquals(1000, viewModel.stepCount.value)
    }

    @Test
    fun `fetchStepGoal updates stepGoal`() = runTest(testDispatcher) {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 5000,
            activityGoal = 2.0
        )

        coEvery { personDao.getPersonById(any()) } returns flow { emit(person) }

        viewModel.fetchStepGoal()
        advanceUntilIdle()

        assertEquals(5000, viewModel.stepGoal.value)
    }

    @Test
    fun `fetchLastSevenDaysSteps updates lastSevenDaysSteps`() = runTest(testDispatcher) {
        val stepsList = (1..7).map {
            val date = Calendar.getInstance().apply { add(Calendar.DATE, -it) }
            val formattedDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date.time)
            "$formattedDate: ${it * 1000}"
        }
        val fitnessData = FitnessData(
            steps = ArrayList(stepsList),
            bpm = arrayListOf("80", "45"),
            sleepTime = arrayListOf("8", "5", "3.4"),
            initialStepCount = -1,
            cumulativeSteps = 0
        )

        coEvery { fitnessDataDao.getSensorData() } returns flow { emit(fitnessData) }

        viewModel.fetchLastSevenDaysSteps()
        advanceUntilIdle()

        assertEquals((1..7).map { it * 1000 }, viewModel.lastSevenDaysSteps.value)
    }

    @Test
    fun `showInfoDialog updates isInfoDialogVisible`() {
        viewModel.showInfoDialog(true)

        assertEquals(true, viewModel.isInfoDialogVisible.value)

        viewModel.showInfoDialog(false)

        assertEquals(false, viewModel.isInfoDialogVisible.value)
    }
}
