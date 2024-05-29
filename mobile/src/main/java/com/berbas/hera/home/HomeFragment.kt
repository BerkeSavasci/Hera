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
import androidx.lifecycle.lifecycleScope
import com.berbas.hera.R
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
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

        // Initialize BluetoothConnection
        bluetoothConnection = BluetoothConnection(requireContext())

        // Initialize ListView and its adapter
        val devicesListView = view.findViewById<ListView>(R.id.devices_list_view)
        devicesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        devicesListView.adapter = devicesAdapter

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

        // Observe scannedDevices state flow
        bluetoothConnection.scannedDevices.onEach { devices ->
            devicesAdapter.clear()
            devicesAdapter.addAll(devices)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
}