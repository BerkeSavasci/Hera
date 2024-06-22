package com.berbas.fittrackapp.screens.goals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.berbas.fittrackapp.R

@Composable
fun GoalsScreen() {
    val badges = listOf(
        Badge(R.drawable.running_badge, "Badge 1", "This is badge 1"),
        Badge(R.drawable.weight_badge, "Badge 2", "This is badge 2"),
        Badge(R.drawable.helthy_badge, "Badge 3", "This is badge 3")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Goals") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 56.dp)
        ) {
            Text(
                text = "Pre selected programs",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            GoalSection(title = "Abnehmen")
            GoalSection(title = "Zunehmen")
            GoalSection(title = "Aktiver werden")
            Text(
                text = "Your Achievements",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            GoalSection(title = "Badges", badges = badges)
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    Column(
        modifier = Modifier
            .size(100.dp)
            .border(
                1.dp,
                Color(40, 40, 40),
                RoundedCornerShape(4.dp)
            )
            .background(color = Color(40, 40, 40))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = badge.imageResourceId),
            contentDescription = badge.name,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun PlaceholderCard() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(
                1.dp,
                Color(40, 40, 40),
                RoundedCornerShape(4.dp)
            )
            .background(color = Color(40, 40, 40))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Program",
            style = MaterialTheme.typography.caption,
            color = Color.White
        )
    }
}

@Composable
fun GoalSection(title: String, badges: List<Badge>? = null) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(horizontal = 8.dp)
            .background(color = Color(30, 30, 30))
            .border(1.dp, Color(30, 30, 30), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badges?.forEach { badge ->
                BadgeItem(badge)
            } ?: run {
                repeat(4) {
                    PlaceholderCard()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoalsScreen() {
    GoalsScreen()
}