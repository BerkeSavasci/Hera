package com.berbas.fittrackapp.screens.connections.bluetooth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berbas.fittrackapp.logger.Logger
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.bluetooth.ConnectionResult
import com.berbas.heraconnectcommon.connection.bluetooth.DataMessage
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class BluetoothSyncViewModelTest {

    private lateinit var viewModel: BluetoothSyncViewModel
    private val testLogger: Logger = mockk(relaxed = true)
    private val personDao = mockk<PersonDao>(relaxed = true)
    private val bluetoothController = mockk<BluetoothControllerInterface>(relaxed = true)
    private val fitnessDao = mockk<FitnessDataDao>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            BluetoothSyncViewModel(personDao, 1, bluetoothController, fitnessDao, testLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `connectToDevice connects successfully and sends data`() = testScope.runTest {
        val device = BluetoothDeviceDomain(name = "Test Device", address = "00:11:22:33:44:55")
        val connectionResult = flow {
            emit(ConnectionResult.ConnectionSuccess)
            emit(ConnectionResult.TransferSuccess(DataMessage("Test Data", "Test Sender")))
        }

        coEvery { bluetoothController.connectToDevice(device) } returns connectionResult
        coEvery { personDao.getPersonById(1) } returns flowOf(
            Person(
                firstname = "placeholder",
                lastname = "placeholder",
                birthday = "01/01/0212",
                gender = "male",
                height = 187,
                weight = 89.0,
                stepGoal = 5000,
                activityGoal = 6.0,
                id = 1
            )
        )
        coEvery { fitnessDao.getSensorData() } returns flowOf(
            FitnessData(
                steps = arrayListOf("5000"),
                bpm = arrayListOf("70"),
                sleepTime = arrayListOf("2"),
                initialStepCount = 0,
                cumulativeSteps = 0,
                id = 1
            )
        )

        viewModel.connectToDevice(device)
        advanceUntilIdle()

        coVerify { bluetoothController.connectToDevice(device) }
        coVerify { bluetoothController.trySendMessage("Person(firstname=placeholder, lastname=placeholder, birthday=01/01/0212, gender=male, height=187, weight=89.0, stepGoal=5000, activityGoal=6.0, id=1)*FitnessData(steps=[5000], bpm=[70], sleepTime=[2], initialStepCount=0, cumulativeSteps=0, id=1)") }

        verify { testLogger.d("BluetoothSyncViewModel", "Connected to device: Test Device") }
        verify {
            testLogger.d(
                "BluetoothSyncViewModel",
                "Sending all data: Person(firstname=placeholder, lastname=placeholder, birthday=01/01/0212, gender=male, height=187, weight=89.0, stepGoal=5000, activityGoal=6.0, id=1)*FitnessData(steps=[5000], bpm=[70], sleepTime=[2], initialStepCount=0, cumulativeSteps=0, id=1)"
            )
        }
        verify {
            testLogger.d(
                "BluetoothSyncViewModel / connect",
                "Data transfer was successful: DataMessage(message=Test Data, senderName=Test Sender)"
            )
        }
    }

    @Test
    fun `connectToDevice fails to connect`() = testScope.runTest {
        // Arrange
        val device = BluetoothDeviceDomain(name = "Test Device", address = "00:11:22:33:44:55")
        val connectionResult = flow {
            emit(ConnectionResult.ConnectionFailure(Exception("Connection Failed").toString()))
        }

        coEvery { bluetoothController.connectToDevice(device) } returns connectionResult

        // Act
        viewModel.connectToDevice(device)
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.connectToDevice(device) }
        verify {
            testLogger.d(
                "BluetoothSyncViewModel",
                "Failed to connect to device: Test Device"
            )
        }
    }

    @Test
    fun `startBluetoothServer starts and handles successful data transfer`() = testScope.runTest {
        // Arrange
        val serverResult = flow {
            emit(ConnectionResult.ConnectionSuccess)
            emit(
                ConnectionResult.TransferSuccess(
                    DataMessage(
                        "Person(firstname=placeholder, lastname=placeholder, birthday=01/01/0212, gender=male, height=187, weight=89.0, stepGoal=5000, activityGoal=6.0, id=1)" +
                                "*FitnessData(steps=[5000], bpm=[70], sleepTime=[2], initialStepCount=0, cumulativeSteps=0, id=1)",
                        "Test Sender"
                    )
                )
            )
        }

        coEvery { bluetoothController.startBluetoothServer() } returns serverResult

        // Act
        viewModel.startBluetoothServer()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.startBluetoothServer() }
        verify {
            testLogger.d(
                "BluetoothSyncViewModel / server",
                "Data transfer was successful: " +
                        "DataMessage(" +
                        "message=Person(firstname=placeholder, lastname=placeholder, birthday=01/01/0212, gender=male, height=187, weight=89.0, stepGoal=5000, activityGoal=6.0, id=1)" +
                        "*FitnessData(steps=[5000], bpm=[70], sleepTime=[2], initialStepCount=0, cumulativeSteps=0, id=1), senderName=Test Sender)"
            )
        }
    }

    @Test
    fun `startBluetoothServer handles connection failure`() = testScope.runTest {
        // Arrange
        val serverResult = flow {
            emit(ConnectionResult.ConnectionFailure(Exception("Server Failed").toString()))
        }

        coEvery { bluetoothController.startBluetoothServer() } returns serverResult

        // Act
        viewModel.startBluetoothServer()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.startBluetoothServer() }
        verify { testLogger.d("BluetoothSyncViewModel", "Server connection failed") }
    }

    @Test
    fun `startDiscovery initiates device discovery`() = testScope.runTest {
        // Act
        viewModel.startDiscovery()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.startDiscovery() }
    }

    @Test
    fun `stopDiscovery stops device discovery`() = testScope.runTest {
        // Act
        viewModel.stopDiscovery()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.stopDiscovery() }
    }

    @Test
    fun `stopBluetoothServer stops server and closes connection`() = testScope.runTest {
        // Arrange
        val serverResult = flow {
            emit(ConnectionResult.ConnectionSuccess)
        }
        coEvery { bluetoothController.startBluetoothServer() } returns serverResult

        // Start the server first
        viewModel.startBluetoothServer()
        advanceUntilIdle()

        // Act
        viewModel.stopBluetoothServer()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.closeConnection() }
    }

    @Test
    fun `release frees up resources`() = testScope.runTest {
        // Act
        viewModel.release()
        advanceUntilIdle()

        // Assert
        coVerify { bluetoothController.release() }
    }
}

