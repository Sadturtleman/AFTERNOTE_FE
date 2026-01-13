package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ExpandableRecordItem(
    date: String,
    tags: String,
    question: String,
    content: String,
    hasImage: Boolean
) {
    // 상태 관리: 펼쳐짐/닫힘
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .animateContentSize( // 애니메이션 적용
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        // --- Header (항상 보임) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                fontSize = 12.sp,
                fontFamily = Sansneo,
                color = Gray4
            )
            Text(
                text = tags,
                fontSize = 12.sp,
                color = Gray4,
                fontFamily = Sansneo
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }, // 클릭 시 토글
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Sansneo,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = B3,
                modifier = Modifier.size(24.dp)
            )
        }

        // --- Body (펼쳐졌을 때만 보임) ---
        if (expanded) {
            Spacer(modifier = Modifier.height(16.dp))

            if (hasImage) {
                // 1. 이미지 카드 형태
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray) // 실제 이미지가 없을 경우 회색 배경
                ) {
                    // 실제 이미지 사용 시:
                    // Image(painter = painterResource(id = R.drawable.sample_img), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())

                    // 예시용 플레이스홀더 (그라데이션 효과 흉내)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF555555)) // 이미지 대신 어두운 배경
                    )
                }
            } else {
                // 2. 빈 박스 형태 (일기장)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, B3, RoundedCornerShape(12.dp)) // 파란 테두리
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Gray9,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
