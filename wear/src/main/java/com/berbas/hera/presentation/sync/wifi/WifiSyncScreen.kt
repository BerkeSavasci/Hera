package com.berbas.hera.presentation.sync.wifi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun WifiSyncScreen(
    viewModel: WifiSyncViewModel,
    onBackClick: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            WifiItem(
                label = "Send",
                viewModel.send()
            )
        }
        item {
            WifiItem(
                label = "Receive",
                viewModel.receive()
            )
        }
    }
}

@Composable
fun WifiItem(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
    ) {
        Text(text = label)
    }
}