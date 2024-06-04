package com.berbas.hera.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.berbas.hera.R
import com.berbas.heraconnectcommon.data.UserDataController
import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDataBase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    //TODO Allow the user to edit their profile information

    private var personID: Int = 0
    private var isPersonInitialized = false

    private val db by lazy {
        Room.databaseBuilder(
            requireContext(),
            PersonDataBase::class.java,
            "person.db"
        ).build()
    }

    private val controller by lazy {
        UserDataController(db.dao, personID)
    }

    private lateinit var person: Person


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        personID = arguments?.getInt("personID") ?: 0
        Log.d(TAG, "onCreate: personID: $personID")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderValue = view.findViewById<TextView>(R.id.genderValue)
        val birthdayValue = view.findViewById<TextView>(R.id.birthdayValue)
        val weightValue = view.findViewById<TextView>(R.id.weightValue)
        val heightValue = view.findViewById<TextView>(R.id.heightValue)

        val genderCard = view.findViewById<CardView>(R.id.genderCard)
        val birthdayCard = view.findViewById<CardView>(R.id.birthdayCard)
        val weightCard = view.findViewById<CardView>(R.id.weightCard)
        val heightCard = view.findViewById<CardView>(R.id.heightCard)

        genderCard.setOnClickListener { onGenderCardClicked() }
        birthdayCard.setOnClickListener { onBirthdayCardClicked() }
        weightCard.setOnClickListener { onWeightCardClicked() }
        heightCard.setOnClickListener { onHeightCardClicked() }

        lifecycleScope.launch {
            if (!isPersonInitialized){
                initializePerson()
        }
            // Now you can safely access the person object
            heightValue.text = person.height
            weightValue.text = person.weight
            birthdayValue.text = person.birthday
            genderValue.text = person.gender
        }
    }

    /**
     * Show a dialog to allow the user to select their gender
     * and call the controller
     */
    private fun onGenderCardClicked() {
        val options = arrayOf("Male", "Female", "Other")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Gender")
        builder.setItems(options) { dialog, which ->
            lifecycleScope.launch {
                controller.setGender(options[which])
            }
            dialog.dismiss()
        }
        builder.show()
    }

    /**
     * Show a date picker dialog to allow the user to select their birthday
     */
    private fun onBirthdayCardClicked() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                lifecycleScope.launch {
                    controller.setBirthDate(
                        Date.from(
                            selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )
                    )
                }
            }, year, month, day)

        datePickerDialog.show()
    }

    /**
     * Show a number picker dialog to allow the user to select their weight
     */
    private fun onWeightCardClicked() {
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 300
            value = person.weight.toInt()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Weight")
        builder.setView(numberPicker)
        builder.setPositiveButton("OK") { dialog, _ ->
            val weight = numberPicker.value.toDouble()
            lifecycleScope.launch {
                controller.setWeight(weight)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * Show a number picker dialog to allow the user to select their height
     */
    private fun onHeightCardClicked() {
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 300
            value = person.height.toInt()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Height")
        builder.setView(numberPicker)
        builder.setPositiveButton("OK") { dialog, _ ->
            val height = numberPicker.value.toDouble()
            lifecycleScope.launch {
                controller.setHeight(height)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private suspend fun initializePerson() {
        val existingPerson = controller.getPersonById(personID)
        if (existingPerson == null) {
            // If not, create a new Person object
            val newPerson = Person(
                firstname = "John",
                lastname = "Doe",
                birthday = "2000-01-01",
                gender = "Male",
                height = "180",
                weight = "80"
            )
            controller.upsertPerson(newPerson)
            person = newPerson
        } else {
            person = existingPerson
        }
        isPersonInitialized = true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param personID the ID of the person to display the profile for
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance(personID: Int) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("personID", personID)
                }
            }
    }
}