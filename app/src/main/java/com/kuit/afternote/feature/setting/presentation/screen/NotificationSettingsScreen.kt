package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.setting.presentation.component.InfoBulletItem
import com.kuit.afternote.feature.setting.presentation.component.MarketingOptionRow
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen() {
    val scrollState = rememberScrollState()

    // 상태 관리
    var isSmsEnabled by remember { mutableStateOf(true) }
    var isEmailEnabled by remember { mutableStateOf(true) }
    var isPushEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar("푸시 알림 설정") {}
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 섹션 1: 기기 알림 설정
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "기기 알림 설정",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo,
                    color = Gray9,
                    modifier = Modifier.clickable {}
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("꺼짐", fontSize = 14.sp, color = Gray5, fontFamily = Sansneo)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "전체 알림 수신 설정은 기기 알림 설정에서 가능합니다.",
                fontSize = 14.sp,
                color = Gray9,
                fontFamily = Sansneo
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Gray3)
            Spacer(modifier = Modifier.height(16.dp))

            // 섹션 2: 마케팅 및 광고 알림 설정
            Text("마케팅 및 광고 알림 설정", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Gray9, fontFamily = Sansneo)
            Spacer(modifier = Modifier.height(24.dp))

            MarketingOptionRow("문자 메시지", isSmsEnabled) { isSmsEnabled = it }
            MarketingOptionRow("E-mail", isEmailEnabled) { isEmailEnabled = it }
            MarketingOptionRow("푸시 알림", isPushEnabled) { isPushEnabled = it }

            Spacer(modifier = Modifier.height(24.dp))

            // 섹션 3: 유의사항 안내 (불렛 포인트)
            val infoItems = listOf(
                "서비스의 중요 안내사항 및 수신인 관련 정보는 위 수신 여부와 관계없이 발송됩니다.",
                "문자, 이메일, 푸시 메세지 수신을 선택하면 광고성 정보 수신에 자동 동의 처리됩니다.",
                "앱 푸시 알림은 애프터노트 앱 로그인 > 설정 > 푸시 알림 설정에서 알림을 끌 수 있습니다."
            )

            infoItems.forEach { text ->
                InfoBulletItem(text)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview
@Composable
private fun NotificationSettingsScreenPreview() {
    NotificationSettingsScreen()
}
