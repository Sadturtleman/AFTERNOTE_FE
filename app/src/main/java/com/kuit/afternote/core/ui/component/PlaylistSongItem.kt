package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 플레이리스트·노래 추가 등에서 공통으로 쓰는 노래 한 줄 아이템.
 *
 * UI: 앨범 48dp(DarkGray placeholder), 제목 Bold 14sp Gray9, 가수 12sp Gray6.
 * 구분선은 상위에서 [androidx.compose.material3.HorizontalDivider]로 처리.
 *
 * - [onClick]이 있으면 클릭 가능, [trailingContent]로 라디오 버튼 등 오른쪽 UI 삽입 (없으면 null)
 *
 * @param song 표시용 모델 [PlaylistSongDisplay] (Feature별 Song/Entity에서 매핑)
 * @param displayIndex 목록 내 순번 (이미지/placeholder용, 현재는 미사용, API 호환용)
 * @param onClick 클릭 시 호출 (null이면 비클릭)
 * @param trailingContent Row 오른쪽 콘텐츠 (AddSongScreen·MemorialPlaylistRouteScreen에서는 라디오)
 */
@Composable
fun PlaylistSongItem(
    song: PlaylistSongDisplay,
    displayIndex: Int,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val base = if (onClick != null) {
        Modifier.fillMaxWidth().clickable(onClick = onClick)
    } else {
        Modifier.fillMaxWidth()
    }

    Column(modifier = base) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.DarkGray)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Bold,
                        color = Gray9
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray6
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
        song = PlaylistSongDisplay(id = "1", title = "노래 제목", artist = "가수 이름"),
        displayIndex = 1
    )
}
