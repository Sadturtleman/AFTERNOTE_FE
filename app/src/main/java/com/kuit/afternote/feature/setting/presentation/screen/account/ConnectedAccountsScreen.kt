package com.kuit.afternote.feature.setting.presentation.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.ThumbSwitch
import com.kuit.afternote.feature.setting.presentation.uimodel.SnsAccount
import com.kuit.afternote.ui.theme.Sansneo

// 1. 데이터 모델 정의
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedAccountsScreen() {
    Scaffold(
        topBar = {
            TopBar("연결된 게정") {
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 헤더 섹션
            Text("SNS 계정 연결", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = Sansneo)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "자주 사용하는 소설 계정을 연결하고,\n빠르게 로그인 할 수 있습니다.",
                fontSize = 14.sp,
                fontFamily = Sansneo
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 리스트 구현
            val accounts = listOf(
                SnsAccount("네이버 계정으로 로그인", Color(0xFF03C75A), false),
                SnsAccount("구글 계정으로 로그인", Color(0xFFF2F2F2), true),
                SnsAccount("카카오톡 계정으로 로그인", Color(0xFFFEE500), true),
                SnsAccount("애플 계정으로 로그인", Color(0xFF000000), false)
            )

            accounts.forEach { account ->
                SnsAccountRow(account)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SnsAccountRow(account: SnsAccount) {
    var isChecked by remember { mutableStateOf(account.initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // SNS 로고 아이콘 (Circle)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(account.iconBackground, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // 실제 구현 시 Image(painterResource(id = account.iconRes), ...) 사용
            Text(
                text = account.name.first().toString(),
                color = if (account.iconBackground == Color(0xFFFEE500)) Color.Black else Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 텍스트 라벨
        Text(
            text = account.name,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Sansneo
        )

        // 스위치 (iOS 스타일과 유사하게 커스텀 가능하나 기본 Material3 Switch 사용)
        ThumbSwitch(
            checked = isChecked,
            onCheckedChange = { isChecked = !isChecked }
        )
    }
}

@Preview
@Composable
private fun ConnectedAccountsScreenPreview() {
    ConnectedAccountsScreen()
}
