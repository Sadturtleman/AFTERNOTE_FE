package com.kuit.afternote.feature.setting.presentation.screen.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.SettingRow
import com.kuit.afternote.feature.setting.presentation.uimodel.SettingItemData
import com.kuit.afternote.feature.setting.presentation.uimodel.SettingSection
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun SettingMainScreen(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val sections = listOf(
        SettingSection(
            "계정",
            listOf(
                SettingItemData("프로필 수정"),
                SettingItemData("비밀번호 변경"),
                SettingItemData("연결된 계정"),
                SettingItemData("알림 설정"),
                SettingItemData(stringResource(R.string.setting_logout))
            )
        ),
        SettingSection(
            "수신자 관리",
            listOf(
                SettingItemData("수신자 목록"),
                SettingItemData("수신자 등록"),
                SettingItemData("사후 전달 조건")
            )
        ),
        SettingSection(
            "보안",
            listOf(
                SettingItemData("앱 잠금 설정", status = "켬")
            )
        ),
        SettingSection(
            "고객 지원",
            listOf(
                SettingItemData("FAQ"),
                SettingItemData("1:1 문의"),
                SettingItemData("공지사항"),
                SettingItemData("이용약관"),
                SettingItemData("개인정보 취급방침"),
                SettingItemData("애프터노트 서비스 안내")
            )
        )
    )

    Scaffold(
        topBar = {
            TopBar(title = "설정")
        },
        bottomBar = { BottomNavigationBar(selectedItem = BottomNavItem.HOME) { } }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
        ) {
            sections.forEach { section ->
                // 섹션 헤더
                section.header?.let {
                    item {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Sansneo
                        )
                    }
                }

                // 섹션 내 아이템들
                itemsIndexed(section.items) { index, item ->
                    SettingRow(
                        item = item,
                        showDivider = index != section.items.lastIndex,
                        onClick = { onClick(item.title) }
                    )
                }

                // 섹션 간 간격
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Preview
@Composable
private fun SettingMainScreenPreview() {
    SettingMainScreen(onClick = {})
}
