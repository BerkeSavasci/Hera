package com.berbas.hera.profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.berbas.hera.R
import com.berbas.heraconnectcommon.data.UserDataController
import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDataBase
import kotlinx.coroutines.launch


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
                    firstname = "Mary",
                    lastname = "Jane",
                    birthday = "1990-01-01",
                    gender = "Female",
                    height = "199",
                    weight = "75"
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