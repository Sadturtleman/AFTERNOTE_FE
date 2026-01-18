package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 마음기록 리스트 아이템 컴포넌트
 *
 * 피그마 디자인:
 * - 흰색 배경, 둥근 모서리 8dp
 * - 왼쪽: 제목, 부연 설명
 * - 오른쪽: 화살표 버튼 (24x24, 파란색)
 */
@Composable
fun RecordItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.05f),
                blur = 5.dp,
                offsetY = 2.dp,
                offsetX = 0.dp,
                spread = 0.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    color = Black,
                    fontFamily = Sansneo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, // 텍스트가 최대 한 줄로 표시
                    overflow = TextOverflow.Ellipsis //한 줄 안에 못 들어가면, 잘린 부분을 ..으로 표시
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Gray5,
                    fontSize = 12.sp,
                    fontFamily = Sansneo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(B2)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_forward_b2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(6.dp, 12.dp)
                        .offset(x = 9.9.dp, y = 6.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable fun RecordItemPreview()
{
    Column {
        RecordItem(
            title = "데일리 질문 답변",
            subtitle = "매일 다른 질문들에 나를 남겨 보세요.",
            onClick = {}
        )
        Spacer(modifier = Modifier
            .width(8.dp)
        )
        RecordItem(
            title = "일기",
            subtitle = "나의 매일을 기록하세요",
            onClick = {}
        )
        RecordItem(
            title = "깊은 생각",
            subtitle = "오늘은 어떤 생각을 하고 있나요?",
            onClick = {}
        )
    }

}
