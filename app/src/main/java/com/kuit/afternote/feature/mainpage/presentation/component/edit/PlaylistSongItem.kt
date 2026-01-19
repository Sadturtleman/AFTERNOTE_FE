package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 플레이리스트·추모 플레이리스트 추가 화면에서 공통으로 쓰는 노래 한 줄 아이템.
 *
 * - 앨범 커버(48dp, 개발용 placeholder 1–12), 제목/가수, 1px Gray3 하단 보더
 * - [onClick]이 있으면 클릭 가능, [trailingContent]로 체크박스 등 오른쪽 UI 삽입
 *
 * @param song 노래 정보
 * @param displayIndex 목록 내 순번(1부터, placeholder 이미지 선택용)
 * @param onClick 클릭 시 호출 (null이면 비클릭)
 * @param trailingContent Row 오른쪽 콘텐츠 (없으면 null)
 */
@Composable
fun PlaylistSongItem(
    song: Song,
    displayIndex: Int,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            val strokeWidth = 1.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = Gray3,
                start = Offset(x = 0f, y = y),
                end = Offset(x = size.width, y = y),
                strokeWidth = strokeWidth
            )
        }
    val base = if (onClick != null) modifier.clickable(onClick = onClick) else modifier

    Column(modifier = base) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                val placeholderResId = when (((displayIndex - 1) % 12) + 1) {
                    1 -> R.drawable.img_placeholder_1
                    2 -> R.drawable.img_placeholder_2
                    3 -> R.drawable.img_placeholder_3
                    4 -> R.drawable.img_placeholder_4
                    5 -> R.drawable.img_placeholder_5
                    6 -> R.drawable.img_placeholder_6
                    7 -> R.drawable.img_placeholder_7
                    8 -> R.drawable.img_placeholder_8
                    9 -> R.drawable.img_placeholder_9
                    10 -> R.drawable.img_placeholder_10
                    11 -> R.drawable.img_placeholder_11
                    else -> R.drawable.img_placeholder_12
                }
                Image(
                    painter = painterResource(id = placeholderResId),
                    contentDescription = "$displayIndex",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Text(
                    text = song.artist,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray9
                    )
                )
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistSongItemPreview() {
    PlaylistSongItem(
        song = Song(id = "1", title = "노래 제목", artist = "가수 이름"),
        displayIndex = 1
    )
}
