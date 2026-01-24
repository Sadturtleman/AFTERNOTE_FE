package com.kuit.afternote.feature.dev.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 헤더 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "개발자 모드",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 사용자 모드로 이동 버튼 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Button(
                onClick = onUserModeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("사용자 모드로 이동")
            }
        }

        // 화면 버튼들 (2열 그리드)
        items(items = screens) { screen ->
            Button(
                onClick = { onScreenClick(screen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = screen.name,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
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
                ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
                ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
                ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
                ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
                ScreenInfo("스플래시 화면", "dev_splash"),
                ScreenInfo("로그인 화면", "dev_login"),
                ScreenInfo("회원가입 화면", "dev_signup"),
                ScreenInfo("지문 로그인 화면", "fingerprint_login"),
                ScreenInfo("타임레터 화면", "time_letter_main"),
                ScreenInfo("타임레터 작성 화면", "time_letter_writer"),
                ScreenInfo("임시저장 화면", "draft_letter"),
                ScreenInfo("수신자 목록 화면", "receive_list"),
                ScreenInfo("타임레터 빈 화면", "letter_empty"),
                ScreenInfo("설정 화면", "setting_main")
            ),
            onScreenClick = {},
            onUserModeClick = {}
        )
    }
}
