package com.berbas.hera.presentation.data

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel() {

    private val _personState = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _personState

    init {
        observeAndRefreshPerson()
    }

    /**
     * Function to refresh the user data
     */
    private fun observeAndRefreshPerson() {
        viewModelScope.launch {
            personDao.getPersonById(id).collect { person ->
                if (person != null) { // don't simplify
                    _personState.value = ProfileState(
                        firstName = person.firstname,
                        lastName = person.lastname,
                        gender = person.gender,
                        birthday = person.birthday,
                        weight = person.weight,
                        height = person.height,
                        stepGoal = person.stepGoal,
                        activityGoal = person.activityGoal
                    )
                } else {
                    val defaultPerson = Person(
                        id = id,
                        firstname = "Placeholder",
                        lastname = "Placeholder",
                        gender = "Placeholder",
                        birthday = "Placeholder",
                        weight = 0.0,
                        height = 0,
                        stepGoal = 6000,
                        activityGoal = 1.30
                    )
                    personDao.upsertPerson(defaultPerson)
                    _personState.value = ProfileState(
                        firstName = defaultPerson.firstname,
                        lastName = defaultPerson.lastname,
                        gender = defaultPerson.gender,
                        birthday = defaultPerson.birthday,
                        weight = defaultPerson.weight,
                        height = defaultPerson.height,
                        stepGoal = defaultPerson.stepGoal,
                        activityGoal = defaultPerson.activityGoal
                    )
                }
            }
        }
    }

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.SetActivityGoal -> {
                _personState.update {
                    it.copy(
                        activityGoal = event.activityGoal,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetStepGoal -> {
                _personState.update {
                    it.copy(
                        stepGoal = event.stepGoal,
                    )
                }
                updatePersonData()
            }

            else -> {

            }
        }
    }

    /**
     * Function to update the person information in the database
     */
    private fun updatePersonData() {
        viewModelScope.launch {
            val person = Person(
                id = id,
                firstname = _personState.value.firstName,
                lastname = _personState.value.lastName,
                gender = _personState.value.gender,
                birthday = _personState.value.birthday,
                weight = _personState.value.weight,
                height = _personState.value.height,
                stepGoal = _personState.value.stepGoal,
                activityGoal = _personState.value.activityGoal
            )
            personDao.upsertPerson(person)
        }
    }
}