package com.berbas.fittrackapp.screens.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IProfileViewModel {

    val state: StateFlow<ProfileState>

    fun onEvent(event: PersonEvent)
}