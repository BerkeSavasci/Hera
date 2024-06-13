package com.berbas.fittrackapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berbas.fittrackapp.screens.profile.ProfileViewModel
import com.berbas.fittrackapp.ui.theme.HeraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeraTheme {
                val profileViewModel: ProfileViewModel = viewModel()
                MainScreen(profileViewModel)

            }
        }
    }
}