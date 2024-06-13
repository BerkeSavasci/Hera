package com.berbas.fittrackapp.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.data.annotations.UserId
import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ProfileScreen manages the data for the view
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel() {

    /**
     * MutableStateFlow to hold the state of the profile of the person
     */
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        refreshUser()
    }

    /**
     * Function to refresh the user data
     */
    private fun refreshUser() {
        viewModelScope.launch {
            val person = personDao.getPersonById(id).firstOrNull() ?: Person(
                id = id,
                firstname = "Placeholder",
                lastname = "Placeholder",
                gender = "Placeholder",
                birthday = "Placeholder",
                weight = 0.0,
                height = 0.0
            )
            _state.value = ProfileState(
                firstName = person.firstname,
                lastName = person.lastname,
                gender = person.gender,
                birthday = person.birthday,
                weight = person.weight,
                height = person.height
            )
            personDao.upsertPerson(person)
        }
    }

    /**
     * Function to handle events from the view
     */
    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.SetFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName,
                        isEditingFields = true
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName,
                        isEditingFields = true
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetGender -> {
                _state.update {
                    it.copy(
                        gender = event.gender,
                        isEditingFields = true
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetBirthday -> {
                _state.update {
                    it.copy(
                        birthday = event.birthday,
                        isEditingFields = true
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetHeight -> {
                _state.update {
                    it.copy(
                        height = event.height,
                        isEditingFields = true
                    )
                }
                updatePersonData()
            }

            is PersonEvent.SetWeight -> {
                _state.update {
                    it.copy(
                        weight = event.weight,
                        isEditingFields = true
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
                firstname = _state.value.firstName,
                lastname = _state.value.lastName,
                gender = _state.value.gender,
                birthday = _state.value.birthday,
                weight = _state.value.weight,
                height = _state.value.height
            )
            personDao.upsertPerson(person)
        }
    }

}

