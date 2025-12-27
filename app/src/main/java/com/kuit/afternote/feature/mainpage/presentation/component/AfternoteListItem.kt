package com.kuit.afternote.feature.mainpage.presentation.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Black9
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 애프터노트 리스트 아이템 컴포넌트
 *
 * 피그마 디자인:
 * - 흰색 배경, 둥근 모서리 16dp
 * - 왼쪽: 아이콘 (40x40)
 * - 중간: 제목, 날짜
 * - 오른쪽: 화살표 버튼 (24x24, 파란색)
 */
@Composable
fun AfternoteListItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    imageRes: Int? = null,
    iconVector: ImageVector? = null,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(74.dp)
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.05f),
                blur = 5.dp,
                offsetY = 2.dp,
                offsetX = 0.dp,
                spread = 0.dp
            ).clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽: 아이콘/이미지 영역
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            iconVector != null -> Color(0xFFE3F2FD)
                            imageRes != null -> Color.Transparent
                            else -> Color(0xFFE3F2FD)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    iconVector != null -> {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = B2,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    imageRes != null -> {
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Inside
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 중간: 텍스트 영역
            Column(
                modifier = Modifier
                    .weight(1f)
//                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = Black9,
                    fontSize = 16.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "최종 작성일 $date",
                    color = Gray5,
                    fontSize = 10.sp,
                    fontFamily = Sansneo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 오른쪽: 화살표 버튼
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(B2)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_listitemarrow),
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
@Composable
private fun AfternoteListItemPreview() {
    Column {
        AfternoteListItem(
            title = "인스타그램",
            date = "2023.11.24",
            imageRes = getIconResForTitle("인스타그램")
        )
        Spacer(modifier = Modifier.height(8.dp))
        AfternoteListItem(
            title = "갤러리",
            date = "2023.11.25",
            imageRes = getIconResForTitle("갤러리")
        )
    }
}
