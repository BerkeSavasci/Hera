package com.berbas.fittrackapp.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ProfileScreen manages the data for the view
 */
@HiltViewModel
open class ProfileViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel(), IProfileViewModel {

    /**
     * MutableStateFlow to hold the state of the profile of the person
     */
    private val _personState = MutableStateFlow(ProfileState())
    override val state: StateFlow<ProfileState> = _personState

    init {
        observeAndRefreshPerson()
    }

    /**
     * Function to refresh the user data
     */
    fun observeAndRefreshPerson() {
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
                        weight = 1.1,
                        height = 1,
                        stepGoal = 6000,
                        activityGoal = 1.5
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

    /**
     * Function to handle events from the view
     */
    override fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.SetFirstName -> {
                if (event.firstName.isNotBlank()) {
                    _personState.update {
                        it.copy(
                            firstName = event.firstName,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetLastName -> {
                if (event.lastName.isNotBlank()) {
                    _personState.update {
                        it.copy(
                            lastName = event.lastName,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetGender -> {
                if (event.gender.isNotBlank()) {
                    _personState.update {
                        it.copy(
                            gender = event.gender,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetBirthday -> {
                if (event.birthday.isNotBlank()) {
                    _personState.update {
                        it.copy(
                            birthday = event.birthday,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetHeight -> {
                if (event.height > 0) {
                    _personState.update {
                        it.copy(
                            height = event.height,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetWeight -> {
                if (event.weight > 0) {
                    _personState.update {
                        it.copy(
                            weight = event.weight,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetStepGoal -> {
                if (event.stepGoal > 0) {
                    _personState.update {
                        it.copy(
                            stepGoal = event.stepGoal,
                        )
                    }
                    updatePersonData()
                }
            }

            is PersonEvent.SetActivityGoal -> {
                if (event.activityGoal > 0) {
                    _personState.update {
                        it.copy(
                            activityGoal = event.activityGoal,
                        )
                    }
                    updatePersonData()
                }
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
