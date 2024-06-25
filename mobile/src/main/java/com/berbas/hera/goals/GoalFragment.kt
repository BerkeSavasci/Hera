package com.berbas.hera.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.berbas.hera.R
import com.berbas.heraconnectcommon.localData.person.PersonDataBase

/**
 * A simple [Fragment] subclass.
 * Use the [GoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalFragment : Fragment() {
    private var personID: Int = 0
    private lateinit var db: PersonDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param db the person Database
         * @return A new instance of fragment GoalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(personID: Int,db: PersonDataBase) =
            GoalFragment().apply {
                this.personID = personID
                this.db = db
            }
    }
}