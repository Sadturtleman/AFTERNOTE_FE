package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.mainpage.presentation.component.common.header.MainHeader
import com.kuit.afternote.feature.mainpage.presentation.component.fingerprint.FingerprintAuthContent
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1

/**
 * 지문 로그인 화면
 *
 * 피그마 디자인 기반:
 * - 헤더: "지문 로그인" 타이틀
 * - 안내 텍스트: "사용자 인증 후 조회가 가능합니다."
 * - 지문 아이콘 (중앙)
 * - 버튼: "지문 인증하기" (B3 배경색)
 * - 하단 네비게이션 바
 */
@Composable
fun FingerprintLoginScreen(
    modifier: Modifier = Modifier,
    onFingerprintAuthClick: () -> Unit = {}
) {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
//                .padding(paddingValues)
        ) {
            // 헤더
            MainHeader(title = "지문 로그인")

            // 메인 컨텐츠
            FingerprintAuthContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onFingerprintAuthClick = onFingerprintAuthClick
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun FingerprintLoginScreenPreview() {
    AfternoteTheme {
        FingerprintLoginScreen()
    }
}
