package com.berbas.fittrackapp.ui.viewModelTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berbas.fittrackapp.screens.connections.wifi.WifiSyncViewModel
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import com.berbas.heraconnectcommon.localData.person.PersonDao
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class WifiSyncViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WifiSyncViewModel
    private val personDao = mock(PersonDao::class.java)
    private val wifiController = mock(WifiConnectionInterface::class.java)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        viewModel = WifiSyncViewModel(personDao, 1, wifiController)
    }

    @Test
    fun sendData_shouldShowToastAndLog() = runTest(testDispatcher) {
        // Set up your mocks and verify expected interaction
    }
}