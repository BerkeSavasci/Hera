package com.berbas.hera.presentation.data

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
class UserDataViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel() {
    /**
     * MutableStateFlow to hold the state of the profile of the person
     */
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
            }
        }
    }

    /**
     * Function to handle events from the view
     */
    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.SetFirstName -> {
                _personState.update {
                    it.copy(
                        firstName = event.firstName,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetLastName -> {
                _personState.update {
                    it.copy(
                        lastName = event.lastName,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetGender -> {
                _personState.update {
                    it.copy(
                        gender = event.gender,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetBirthday -> {
                _personState.update {
                    it.copy(
                        birthday = event.birthday,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetHeight -> {
                _personState.update {
                    it.copy(
                        height = event.height,
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetWeight -> {
                _personState.update {
                    it.copy(
                        weight = event.weight,
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

            is PersonEvent.SetActivityGoal -> {
                _personState.update {
                    it.copy(
                        activityGoal = event.activityGoal,
                    )
                }
                updatePersonData()
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