package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.ExpandableRecordItem
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9

@Composable
fun MindRecordDetailScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }

    Scaffold(
        topBar = {
            TopBar(
                title = "마음의 기록"
            ) { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 상단 날짜 선택 버튼 영역
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                OutlinedButton(
                    onClick = { /* 날짜 변경 로직 */ },
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(B3)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "11월 10일",
                        fontSize = 13.sp,
                        color = Gray9,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = B3,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 리스트 영역
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // 첫 번째 항목 (사진 + 텍스트)
                item {
                    ExpandableRecordItem(
                        date = "2025년 11월 10일",
                        tags = "#감사 #가족",
                        question = "오늘 하루, 누구에게 가장 고마웠나요?",
                        content = "아무 말 없이 내 옆을 지켜주는 남편이 너무 고맙다. 힘든 내색 없이 묵묵히 함께해주는 것만으로도 큰 힘이 된다.",
                        hasImage = true
                    )
                    HorizontalDivider(color = Gray6, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                }

                // 두 번째 항목 (박스 + 텍스트)
                item {
                    ExpandableRecordItem(
                        date = "2025년 11월 10일",
                        tags = "#일상",
                        question = "오늘의 일기",
                        content = "오늘은 병원에서 검사 결과가 안좋게 나와서 남편이 우울해하는거 같길래 치킨을 시켜줬다.",
                        hasImage = false // 일기 박스 형태
                    )
                    HorizontalDivider(color = Gray6, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMindRecordDetail() {
    MaterialTheme {
        MindRecordDetailScreen()
    }
}
