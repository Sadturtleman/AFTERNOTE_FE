package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

/**
 * 이미지 없을 때 배경 테마 (배경색 + 로고 세트)
 */
enum class LetterTheme(
    val backgroundColor: Color,
    // 로고 리소스
    val logoResId: Int
) {
    PEACH(
        backgroundColor = Color(0xFFFFE1CC),
        logoResId = R.drawable.logo_yellow
    ),
    BLUE(
        backgroundColor = Color(0xFFBDE0FF),
        logoResId = R.drawable.logo_orange
    ),
    YELLOW(
        backgroundColor = Color(0xFFFFDC63),
        logoResId = R.drawable.logo_blue
    )
}

@Composable
fun TimeLetterBlockItem(
    receiverName: String,
    sendDate: String,
    title: String,
    content: String,
    // 배경 이미지 (null이면 테마 사용)
    imageResId: Int?,
    // 이미지 없을 때 테마
    theme: LetterTheme = LetterTheme.BLUE,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // 텍스트 색상
    val textColor = Color(0xFF757575) // 항상 gray-6
    val titleColor = Color.White // 항상 #FFF
    val contentColor = if (imageResId != null) Color(0xFFE0E0E0) else Color(0xFF757575) // gray-3

    Box(
        modifier = modifier
            .width(350.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        // 배경 처리
        if (imageResId != null) {
            // 이미지가 있을 때: 이미지 + 그라데이션
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // 그라데이션 오버레이 (위: 투명, 아래: 40% 검정)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x660E0E0E)
                            )
                        )
                    )
            )
        } else {
            // 이미지가 없을 때: 테마 배경색 + 그라데이션 + 로고
            // 1. 배경색 20% 투명도
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(theme.backgroundColor.copy(alpha = 0.2f))
            )
            // 2. 그라데이션 오버레이 (50% 투명도)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.00f to Color(0x000E0E0E), // 0%
                                0.09f to Color(0x170E0E0E), // 9%
                                0.45f to Color(0x730E0E0E), // 45%
                                1.00f to Color(0x800E0E0E) // 50% (전체 50%)
                            )
                        )
                    )
            )
            // 3. 로고 (중앙)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = theme.logoResId),
                    contentDescription = "로고"
                )
            }
        }

        // 텍스트 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(19.dp)
        ) {
            // 상단: 수신인 & 발송 예정일
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "수신인: $receiverName",
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp,
                    fontFamily = FontFamily(Font(R.font.sansneomedium))
                )
                Text(
                    text = "발송 예정일: $sendDate",
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp,
                    fontFamily = FontFamily(Font(R.font.sansneomedium))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 하단: 제목 (BodyBase)
            Text(
                text = title,
                color = titleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium, // 500
                lineHeight = 22.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 하단: 내용 미리보기 (CaptionLarge-R)
            Text(
                text = content,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal, // 400
                lineHeight = 18.sp,
                maxLines = 1, // white-space: nowrap
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily(Font(R.font.sansneoregular))
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun LetterBlockWithImagePreview() {
    TimeLetterBlockItem(
        receiverName = "박채연",
        sendDate = "2027. 11. 24",
        title = "채연아 20번째 생일을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
        imageResId = R.drawable.ic_test_block, // 이미지 있음 → 그라데이션 적용
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun LetterBlockPeachThemePreview() {
    TimeLetterBlockItem(
        receiverName = "박채연",
        sendDate = "2027. 11. 24",
        title = "채연아 20번째 생일을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
        imageResId = null,
        theme = LetterTheme.PEACH // Y1 로고 + 살구색
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun LetterBlockBlueThemePreview() {
    TimeLetterBlockItem(
        receiverName = "박채연",
        sendDate = "2027. 11. 24",
        title = "채연아 20번째 생일을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
        imageResId = null,
        theme = LetterTheme.BLUE // White 로고 + 연파랑
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun LetterBlockYellowThemePreview() {
    TimeLetterBlockItem(
        receiverName = "박채연",
        sendDate = "2027. 11. 24",
        title = "채연아 20번째 생일을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
        imageResId = null,
        theme = LetterTheme.YELLOW // Orange 로고 + 노란색
    )
}
