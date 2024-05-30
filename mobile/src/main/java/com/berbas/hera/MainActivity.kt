package com.berbas.hera

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.berbas.hera.databinding.ActivityMainBinding
import com.berbas.hera.goals.GoalFragment
import com.berbas.hera.home.HomeFragment
import com.berbas.hera.profile.ProfileFragment
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import androidx.health.connect.client.HealthConnectClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothConnection: BluetoothConnection
    private lateinit var devicesAdapter: ArrayAdapter<BluetoothDeviceDomain>
    private lateinit var healthConnectClient: HealthConnectClient

    private var userID: Int = 0

    var bluetoothEnabled = false

    private val enableBluetoothResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                bluetoothEnabled = true
            } else {
                bluetoothEnabled = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the bluetooth connection and the adapter for the devices
        bluetoothConnection = BluetoothConnection(this)
        devicesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        //get user id somehow for later
        //for now just hardcode it
        userID = 2

        // reference to the fragments so they don't get recreated every time
        val homeFragment = HomeFragment.newInstance(bluetoothConnection, devicesAdapter)
        val goalFragment = GoalFragment.newInstance("placeholder", "placeholder")
        val profileFragment = ProfileFragment.newInstance(userID)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFragment)
        binding.bottomNavigationView.selectedItemId = R.id.home

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.goals -> replaceFragment(goalFragment)
                R.id.profile -> replaceFragment(profileFragment)
                else -> {

                }
            }
            true
        }

        // get the bluetooth adapter and check if it is enabled
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                    ),
                    0
                )
            }
        } else {
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothResultLauncher.launch(enableBtIntent)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothResultLauncher.launch(enableBtIntent)
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothConnection.release()
    }

    // TODO: Health Connect Permissions request
/*    private fun launchHealthConnectPermissions() {
        healthConnectClient.permissionController.requestPermissions(
            permissions = setOf(
                StepsRecord::class,
                HeartRateRecord::class,
                SleepSessionRecord::class
            ),
            permissionsLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                if (result.all { it.value }) {
                    // Permissions granted
                    readFitnessData()
                } else {
                    // Handle permission denial
                    Log.d("Permissions", "Not all permissions granted")
                }
            }
        )
    }*/
}