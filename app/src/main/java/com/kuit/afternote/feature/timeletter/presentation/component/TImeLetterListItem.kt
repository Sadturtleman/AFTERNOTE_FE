package com.kuit.afternote.feature.timeletter.presentation.component

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

/**
 * 타임레터 리스트형 아이템
 */
data class TimeLetterListItemCallbacks(
    val onClick: () -> Unit = {},
    val onEditClick: () -> Unit = {},
    val onDeleteClick: () -> Unit = {}
)

private const val TAG = "TimeLetterListItem"

@Composable
fun TimeLetterListItem(
    modifier: Modifier = Modifier,
    timeLetter: TimeLetterItem,
    callbacks: TimeLetterListItemCallbacks = TimeLetterListItemCallbacks()
) {
    val imageUrl = timeLetter.mediaUrls.firstOrNull()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = callbacks.onClick)
            .padding(vertical = 12.dp)
    ) {
        // 상단: 수신인 & 발송 예정일
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "수신인: ${timeLetter.receivername}",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
            Text(
                text = "발송 예정일: ${timeLetter.sendDate}",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
        }

        // 이미지가 있으면 표시
        if (!imageUrl.isNullOrBlank() || timeLetter.imageResId != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(248.dp)
                    .width(350.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                when {
                    !imageUrl.isNullOrBlank() -> {
                        val context = LocalContext.current
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUrl)
                                .httpHeaders(
                                    NetworkHeaders.Builder()
                                        .set("User-Agent", "Afternote Android App")
                                        .build()
                                )
                                .build(),
                            contentDescription = "편지 이미지",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            error = painterResource(R.drawable.img_time_letter_placeholder),
                            onError = { state: AsyncImagePainter.State.Error ->
                                Log.e(TAG, "Coil load failed: url=$imageUrl", state.result.throwable)
                            }
                        )
                    }

                    timeLetter.imageResId != null -> {
                        Image(
                            painter = painterResource(id = timeLetter.imageResId),
                            contentDescription = "편지 이미지",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 제목
        Text(
            text = timeLetter.title,
            color = Color(0xFF212121),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sansneomedium)),
            fontWeight = FontWeight(500)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 내용 미리보기
        Text(
            text = timeLetter.content,
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
                painter = painterResource(id = R.drawable.ic_pencil),
                contentDescription = "수정",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = callbacks.onEditClick)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(R.drawable.ic_trash),
                contentDescription = "삭제",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = callbacks.onDeleteClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterListItemWithImagePreview() {
    TimeLetterListItem(
        timeLetter = TimeLetterItem(
            id = "preview",
            receivername = "박채연",
            sendDate = "2027. 11. 24",
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
            imageResId = R.drawable.ic_test_block,
            createDate = "2026. 11. 24"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterListItemWithoutImagePreview() {
    TimeLetterListItem(
        timeLetter = TimeLetterItem(
            id = "preview",
            receivername = "김지은",
            sendDate = "2029. 11. 20",
            title = "지은아 결혼을 축하해",
            content = "너가 태어난 게 엊그제같은데 벌써 결혼이라니..엄마가 없어도 씩씩하게 컸을 지은이를 상상하면 너무 기특해서 안아주고 싶구나 은데 벌써 스...",
            imageResId = null,
            createDate = "2026. 11. 24"
        )
    )
}
