package com.kuit.afternote.feature.dev.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ScreenInfo(
    val name: String,
    val route: String
)

@Composable
fun DevModeScreen(
    screens: List<ScreenInfo>,
    onScreenClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "개발자 모드",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        screens.forEach { screen ->
            Button(
                onClick = { onScreenClick(screen.route) },
                modifier = Modifier.fillMaxSize()
            ) {
                Text(screen.name)
            }
        }
    }
}
