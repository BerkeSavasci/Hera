package com.berbas.hera.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

    private val db by lazy {
        Room.databaseBuilder(
            requireContext(),
            PersonDataBase::class.java,
            "person.db"
        ).build()
    }

    private val controller by lazy {
        UserDataController(db.dao, 1)
    }

    private lateinit var person: Person


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        personID = arguments?.getInt("personID") ?: 0

        lifecycleScope.launch {
            val existingPerson = controller.getPersonById(personID)
            if (existingPerson == null) {
                // If the person does not exist, create a new person
                val newPerson = Person(
                    firstname = " ",
                    lastname = " ",
                    birthday = " ",
                    gender = " ",
                    height = " ",
                    weight = " "
                )
                controller.upsertPerson(newPerson)
            } else {
                person = existingPerson
            }
        }
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
            val weight = controller.getPersonWeightById(personID)
            val height = controller.getPersonHeightById(personID)
            val birthday = controller.getPersonBirthdayById(personID)
            val gender = controller.getPersonGenderById(personID)



            heightValue.text = height
            weightValue.text = weight
            birthdayValue.text = birthday
            genderValue.text = gender
        }
    }

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

    private fun onBirthdayCardClicked() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            lifecycleScope.launch {
                controller.setBirthDate(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            }
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun onWeightCardClicked() {
        val builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val weight = input.text.toString().toDoubleOrNull()
            if (weight != null) {
                lifecycleScope.launch {
                    controller.setWeight(weight)
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun onHeightCardClicked() {
        val builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val height = input.text.toString().toDoubleOrNull()
            if (height != null) {
                lifecycleScope.launch {
                    controller.setHeight(height)
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
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