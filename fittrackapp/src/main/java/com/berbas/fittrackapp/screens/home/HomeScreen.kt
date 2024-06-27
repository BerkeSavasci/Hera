package com.berbas.fittrackapp.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    val stepCount by homeViewModel.stepCount.collectAsState()
    val stepGoal by homeViewModel.stepGoal.collectAsState()
    Log.d("StepCount", "$stepCount")
    Log.d("StepGoal", "$stepGoal")

    androidx.compose.material.Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text("Home") },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "Step Tracker V1",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.size(10.dp))
            CircularProgressIndicator(
                progress = stepCount.toFloat() / stepGoal.toFloat(),
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}
