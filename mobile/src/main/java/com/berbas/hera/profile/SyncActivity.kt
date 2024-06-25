package com.berbas.hera.profile

/*
@AndroidEntryPoint
class SyncActivity : AppCompatActivity() {
    @Inject
    lateinit var bluetoothConnection: BluetoothConnection

    @Inject
    lateinit var devicesAdapter: ArrayAdapter<BluetoothDeviceDomain>

    @Inject
    lateinit var db: PersonDataBase

    private var personID: Int = 0

    private val controller by lazy {
        UserDataController(db.dao, personID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        personID = intent.getIntExtra("personID", 0)
        val devicesListView = findViewById<ListView>(R.id.devices_list_view)
        devicesListView.adapter = devicesAdapter
    }


    private suspend fun getPersonData(personID: Int): String {
        val person = controller.getPersonById(personID)
        Log.d("SyncActivity", "Person data: $person with personID: $personID")

        return "Name: ${person?.firstname} ${person?.lastname}, " +
                "Birthday: ${person?.birthday}, Gender: ${person?.gender}, " +
                "Height: ${person?.height}, Weight: ${person?.weight}"
    }
}
 */