package com.berbas.hera.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.berbas.hera.R
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.ConnectionResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var bluetoothConnection: BluetoothConnection
    private lateinit var devicesAdapter: ArrayAdapter<BluetoothDeviceDomain>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val devicesListView = view.findViewById<ListView>(R.id.devices_list_view)
        devicesListView.adapter = devicesAdapter

        // Set an item click listener on the ListView
        devicesListView.setOnItemClickListener { parent, view, position, id ->
            val device = devicesAdapter.getItem(position)
            // Handle the device click here
            Toast.makeText(context, "Device clicked: ${device?.name}", Toast.LENGTH_SHORT).show()
            if (device != null) {
                bluetoothConnection.connectToDevice(device).onEach { result ->
                    when (result) {
                        is ConnectionResult.ConnectionSuccess -> {
                            Toast.makeText(context, "Connected to device: ${device.name}", Toast.LENGTH_SHORT).show()
                        }
                        is ConnectionResult.ConnectionFailure -> {
                            Toast.makeText(context, "Failed to connect to device: ${device.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        // Handle start scan button click
        val startScanButton = view.findViewById<Button>(R.id.start_scan_button)
        startScanButton.setOnClickListener {
            Log.d("HomeFragment", "Start Scan button clicked")
            bluetoothConnection.startDiscovery()
        }

        // Handle stop scan button click
        val stopScanButton = view.findViewById<Button>(R.id.stop_scan_button)
        stopScanButton.setOnClickListener {
            Log.d("HomeFragment", "Stop Scan button clicked")
            bluetoothConnection.stopDiscovery()
        }

        // Handle start server button click
        val startServerButton = view.findViewById<Button>(R.id.start_server_button)
        startServerButton.setOnClickListener {
            Log.d("HomeFragment", "Start Server button clicked")
            bluetoothConnection.startBluetoothServer().onEach { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Toast.makeText(context, "Server started successfully", Toast.LENGTH_SHORT).show()
                    }
                    is ConnectionResult.ConnectionFailure -> {
                        Toast.makeText(context, "Failed to start server", Toast.LENGTH_SHORT).show()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }


        // Observe scannedDevices state flow
        bluetoothConnection.scannedDevices.onEach { devices ->
            devicesAdapter.clear()
            val devicesWithName = devices.filter { it.name != null}
            devicesAdapter.addAll(devicesWithName)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param bluetoothConnection bluetooth connection instance
         * @param devicesAdapter devices adapter instance
         * @return A new instance of fragment Home.
         */
        @JvmStatic
        fun newInstance(bluetoothConnection: BluetoothConnection, devicesAdapter: ArrayAdapter<BluetoothDeviceDomain>) =
            HomeFragment().apply {
                this.bluetoothConnection = bluetoothConnection
                this.devicesAdapter = devicesAdapter
            }
    }
}