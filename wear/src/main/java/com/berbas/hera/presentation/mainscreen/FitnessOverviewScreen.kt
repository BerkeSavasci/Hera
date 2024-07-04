package com.berbas.hera.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

@Composable
fun FitnessOverviewScreen(
    steps: Int,
    heartRate: Int,
    sleep: String,
    fitnessOverviewViewModel: FitnessOverviewViewModel,
    onStepsClick: () -> Unit,
    onAboutYouClick: () -> Unit,
    aboutYou: String
) {

    val stepCount by fitnessOverviewViewModel.stepCount.collectAsState()
    val stepGoal by fitnessOverviewViewModel.stepGoal.collectAsState()

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            FitnessItem(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                value = "$stepCount",
                unit = " Schritte",
                color = Color.Green,
                onClick = onStepsClick
            )
        }
        item {
            FitnessItem(
                icon = Icons.Default.Favorite,
                value = "$heartRate",
                unit = " bpm",
                color = Color.Red
            )
        }
        item {
            FitnessItem(
                icon = Icons.Default.NightsStay,
                value = sleep,
                unit = "",
                color = Color.Blue
            )
        }
        item {
            FitnessItem(
                icon = Icons.Default.Person,
                value = aboutYou,
                unit = "",
                color = Color.White,
                onClick = onAboutYouClick
            )
        }
    }
}

@Composable
fun FitnessItem(
    icon: ImageVector,
    value: String,
    unit: String,
    color: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(30, 30, 30))
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = unit,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}