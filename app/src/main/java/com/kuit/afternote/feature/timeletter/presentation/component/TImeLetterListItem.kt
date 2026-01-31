package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
 * 타임레터 리스트형 아이템
 */
@Composable
fun TimeLetterListItem(
    receiverName: String,
    sendDate: String,
    title: String,
    content: String,
    imageResId: Int?, // 이미지 (null이면 표시 안 함)
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 상단: 수신인 & 발송 예정일
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "수신인: $receiverName",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
            Text(
                text = "발송 예정일: $sendDate",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
        }

        // 이미지가 있으면 표시
        if (imageResId != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(248.dp)
                    .width(350.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "편지 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 제목
        Text(
            text = title,
            color = Color(0xFF212121),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sansneomedium)),
            fontWeight = FontWeight(500)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 내용 미리보기
        Text(
            text = content,
            color = Color(0xFF757575),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontFamily(Font(R.font.sansneoregular))
        )

        // 수정/삭제 아이콘
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = "수정",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onEditClick)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(R.drawable.trash),
                contentDescription = "삭제",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onDeleteClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterListItemWithImagePreview() {
    TimeLetterListItem(
        receiverName = "박채연",
        sendDate = "2027. 11. 24",
        title = "채연아 20번째 생일을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
        imageResId = R.drawable.ic_test_block
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterListItemWithoutImagePreview() {
    TimeLetterListItem(
        receiverName = "김지은",
        sendDate = "2029. 11. 20",
        title = "지은아 결혼을 축하해",
        content = "너가 태어난 게 엊그제같은데 벌써 결혼이라니..엄마가 없어도 씩씩하게 컸을 지은이를 상상하면 너무 기특해서 안아주고 싶구나 은데 벌써 스...",
        imageResId = null
    )
}
