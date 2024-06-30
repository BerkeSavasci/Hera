package com.berbas.fittrackapp.screens.profile

import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.FakePersonRepository
import com.berbas.heraconnectcommon.localData.person.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Fake ViewModel for the ProfileScreen used for testing the UI
 */
class FakeProfileViewModel(
    private val fakePersonDao: FakePersonRepository,
    private val testUserId: Int
) : ProfileViewModel(fakePersonDao, testUserId) {

    private val _state = MutableStateFlow(
        ProfileState(
            firstName = "Test",
            lastName = "User",
            gender = "male",
            birthday = "01/01/2001",
            weight = 70.0,
            height = 175,
            stepGoal = 10000,
            activityGoal = 2.0
        )
    )
    override val state: StateFlow<ProfileState> = _state

    override fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.SetFirstName -> {
                _state.value = _state.value.copy(firstName = event.firstName)
            }
            is PersonEvent.SetLastName -> {
                _state.value = _state.value.copy(lastName = event.lastName)
            }
            is PersonEvent.SetGender -> {
                _state.value = _state.value.copy(gender = event.gender)
            }
            is PersonEvent.SetBirthday -> {
                _state.value = _state.value.copy(birthday = event.birthday)
            }
            is PersonEvent.SetWeight -> {
                _state.value = _state.value.copy(weight = event.weight)
            }
            is PersonEvent.SetHeight -> {
                _state.value = _state.value.copy(height = event.height)
            }
            is PersonEvent.SetStepGoal -> {
                _state.value = _state.value.copy(stepGoal = event.stepGoal)
            }
            is PersonEvent.SetActivityGoal -> {
                _state.value = _state.value.copy(activityGoal = event.activityGoal)
            }
        }
    }
}