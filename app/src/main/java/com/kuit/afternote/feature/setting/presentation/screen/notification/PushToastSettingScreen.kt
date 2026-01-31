package com.kuit.afternote.feature.setting.presentation.screen.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.ThumbSwitch
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

// 1. 순수한 데이터 모델 (State를 포함하지 않음)
data class PushDomain(
    val name: String,
    val isChecked: Boolean
)

@Composable
fun PushToastSettingScreen(onBackClick: () -> Unit = {}) {
    // 2. 상태 관리: 리스트 전체를 상태로 관리하거나 ViewModel에서 가져옵니다.
    // 여기서는 간단히 SnapshotStateList를 사용해 변경을 감지하게 합니다.
    val domainList = remember {
        mutableStateListOf(
            PushDomain("타임레터", true),
            PushDomain("마음의 기록", true),
            PushDomain("애프터노트", false)
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "푸시 알림 설정",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            domainList.forEachIndexed { index, domain ->
                PushSettingRow(
                    name = domain.name,
                    isChecked = domain.isChecked,
                    onCheckedChange = { newValue ->
                        domainList[index] = domain.copy(isChecked = newValue)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PushSettingRow(
    name: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            // 터치 영역을 Row 전체로 확장하여 UX 개선
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!isChecked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Gray9
        )

        // 중요: 반전된 값을 전달하거나, ThumbSwitch 내부에서 처리된 값을 받아야 함
        ThumbSwitch(
            checked = isChecked,
            onCheckedChange = { newValue -> onCheckedChange(newValue) }
        )
    }
}

@Preview
@Composable
private fun PushToastSettingScreenPreview() {
    PushToastSettingScreen()
}
