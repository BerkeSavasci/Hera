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
import com.berbas.heraconnectcommon.data.UserDataController
import com.berbas.heraconnectcommon.localData.PersonDataBase
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
    private lateinit var db: PersonDataBase
    private var personID: Int = 0

    private val controller by lazy {
        db.dao
    }

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

            if (device != null) {
                bluetoothConnection.connectToDevice(device).onEach { result ->
                    when (result) {
                        is ConnectionResult.ConnectionSuccess -> {
                            Toast.makeText(
                                context,
                                "Connected to device: ${device.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            val message = getPersonData(personID)
                            val sentMessage = bluetoothConnection.trySendMessage(message)
                            sentMessage?.let {
                                Toast.makeText(
                                    context,
                                    "Message sent: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } ?: run {
                                Toast.makeText(
                                    context,
                                    "Failed to send message",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        is ConnectionResult.ConnectionFailure -> {
                            Toast.makeText(
                                context,
                                "Failed to connect to device: ${device.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ConnectionResult.TransferSuccess -> {
                            Toast.makeText(
                                context,
                                "The data transfer was successful",
                                Toast.LENGTH_SHORT
                            ).show()
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
                        Toast.makeText(context, "Server started successfully", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        Toast.makeText(context, "Failed to start server", Toast.LENGTH_SHORT).show()
                    }

                    is ConnectionResult.TransferSuccess -> {
                        Toast.makeText(
                            context,
                            "The data transfer was successful",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }


        // Observe scannedDevices state flow
        bluetoothConnection.scannedDevices.onEach { devices ->
            devicesAdapter.clear()
            val devicesWithName = devices.filter { it.name != null }
            devicesAdapter.addAll(devicesWithName)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return view
    }

    private suspend fun getPersonData(personID: Int): String {
        val person = controller.getPersonById(personID)
        Log.d("HomeFragment", "Person data: $person with personID: $personID")

        return "Name: ${person?.firstname} ${person?.lastname}, " +
                "Birthday: ${person?.birthday}, Gender: ${person?.gender}, " +
                "Height: ${person?.height}, Weight: ${person?.weight}"
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
        fun newInstance(
            personID: Int,
            bluetoothConnection: BluetoothConnection,
            devicesAdapter: ArrayAdapter<BluetoothDeviceDomain>,
            db: PersonDataBase
        ) =
            HomeFragment().apply {
                this.personID = personID
                this.bluetoothConnection = bluetoothConnection
                this.devicesAdapter = devicesAdapter
                this.db = db;
            }
    }
}