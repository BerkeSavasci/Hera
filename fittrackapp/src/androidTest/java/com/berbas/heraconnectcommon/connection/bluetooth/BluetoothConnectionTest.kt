package com.berbas.heraconnectcommon.connection.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.InjectMocks

// FIXME: Sadly I am not able to mock final classes
@Ignore("Cant mock final classes")
@RunWith(MockitoJUnitRunner::class)
class BluetoothConnectionTest {

    private lateinit var context: Context

    @Mock
    private lateinit var bluetoothManager: BluetoothManager

    @Mock
    private lateinit var bluetoothAdapter: BluetoothAdapter

    @Mock
    private lateinit var bluetoothServerSocket: BluetoothServerSocket

    @InjectMocks
    private lateinit var bluetoothConnection: BluetoothConnection


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        context = InstrumentationRegistry.getInstrumentation().targetContext

        `when`(context.getSystemService(BluetoothManager::class.java)).thenReturn(bluetoothManager)
        `when`(bluetoothManager.adapter).thenReturn(bluetoothAdapter)
        `when`(bluetoothAdapter.listenUsingRfcommWithServiceRecord(anyString(), any())).thenReturn(bluetoothServerSocket)

        bluetoothConnection = BluetoothConnection(context)

        mockPermissions(true)
    }

    private fun mockPermissions(granted: Boolean) {
        val permissionState = if (granted) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
        `when`(context.checkSelfPermission(anyString())).thenReturn(permissionState)
    }


    @Test
    fun testStartDiscovery() = runTest {
        `when`(bluetoothAdapter.startDiscovery()).thenReturn(true)
        bluetoothConnection.startDiscovery()
        verify(bluetoothAdapter).startDiscovery()
    }
}