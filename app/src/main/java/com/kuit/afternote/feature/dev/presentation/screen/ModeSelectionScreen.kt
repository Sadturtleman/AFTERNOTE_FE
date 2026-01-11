package com.kuit.afternote.feature.dev.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun ModeSelectionScreen(
    onUserModeClick: () -> Unit,
    onDevModeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("모드 선택")

        Button(
            onClick = onUserModeClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("사용자 모드")
        }

        Button(
            onClick = onDevModeClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("개발자 모드")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModeSelectionScreenPreview() {
    AfternoteTheme {
        ModeSelectionScreen(
            onUserModeClick = {},
            onDevModeClick = {}
        )
    }
}
