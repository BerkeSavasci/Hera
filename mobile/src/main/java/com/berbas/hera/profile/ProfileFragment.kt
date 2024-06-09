package com.berbas.hera.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
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
    private lateinit var genderValue: TextView
    private lateinit var birthdayValue: TextView
    private lateinit var weightValue: TextView
    private lateinit var heightValue: TextView

    private val db by lazy {
        Room.databaseBuilder(
            requireContext(),
            PersonDataBase::class.java,
            "person.db"
        ).fallbackToDestructiveMigration().build()
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

        genderValue = view.findViewById(R.id.genderValue)
        birthdayValue = view.findViewById(R.id.birthdayValue)
        weightValue = view.findViewById(R.id.weightValue)
        heightValue = view.findViewById(R.id.heightValue)

        val genderCard = view.findViewById<CardView>(R.id.genderCard)
        val birthdayCard = view.findViewById<CardView>(R.id.birthdayCard)
        val weightCard = view.findViewById<CardView>(R.id.weightCard)
        val heightCard = view.findViewById<CardView>(R.id.heightCard)

        genderCard.setOnClickListener { onGenderCardClicked() }
        birthdayCard.setOnClickListener { onBirthdayCardClicked() }
        weightCard.setOnClickListener { onWeightCardClicked() }
        heightCard.setOnClickListener { onHeightCardClicked() }

        lifecycleScope.launch {
            if (!isPersonInitialized) {
                initializePerson()
            }
            heightValue.text = person.height.toString() + " cm"
            weightValue.text = person.weight.toString() + " kg"
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
                genderValue.text = options[which]
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
                    birthdayValue.text = controller.getPersonBirthdayById(personID)
                }
            }, year, month, day)

        datePickerDialog.show()
    }

    /**
     * Show a number picker dialog to allow the user to select their weight
     */
    private fun onWeightCardClicked() {
        val integerPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 300
            value = person.weight.toInt()
        }

        val decimalPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 9
            value = ((person.weight - person.weight.toInt()) * 10).toInt()
        }

        val decimalPoint = TextView(requireContext()).apply {
            text = "."
            gravity = Gravity.CENTER
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            addView(integerPicker)
            addView(decimalPoint)
            addView(decimalPicker)
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Weight")
        builder.setView(layout)
        builder.setPositiveButton("OK") { dialog, _ ->
            val weight = integerPicker.value + decimalPicker.value / 10.0
            lifecycleScope.launch {
                controller.setWeight(weight)
                weightValue.text = "$weight kg"
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
        val integerPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 300
            value = person.height.toInt()
        }

        val decimalPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 9
            value = ((person.height - person.height.toInt()) * 10).toInt()
        }

        val decimalPoint = TextView(requireContext()).apply {
            text = "."
            gravity = Gravity.CENTER
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            addView(integerPicker)
            addView(decimalPoint)
            addView(decimalPicker)
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Height")
        builder.setView(layout)
        builder.setPositiveButton("OK") { dialog, _ ->
            val height = integerPicker.value + decimalPicker.value / 10.0
            lifecycleScope.launch {
                controller.setHeight(height)
                heightValue.text = "$height cm"
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
                height = 180.0,
                weight = 80.0
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