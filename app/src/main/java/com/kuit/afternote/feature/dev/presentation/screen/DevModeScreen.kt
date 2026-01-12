package com.kuit.afternote.feature.dev.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

data class ScreenInfo(
    val name: String,
    val route: String
)

@Composable
fun DevModeScreen(
    screens: List<ScreenInfo>,
    onScreenClick: (String) -> Unit,
    onUserModeClick: () -> Unit = {}
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

        // 사용자 모드로 이동 버튼
        Button(
            onClick = onUserModeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("사용자 모드로 이동")
        }

        screens.forEach { screen ->
            Button(
                onClick = { onScreenClick(screen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(screen.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DevModeScreenPreview() {
    AfternoteTheme {
        DevModeScreen(
            screens = listOf(
                ScreenInfo("메인 화면", "main"),
                ScreenInfo("상세 화면", "detail"),
                ScreenInfo("편집 화면", "edit")
            ),
            onScreenClick = {},
            onUserModeClick = {}
        )
    }
}
