package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.expand.horizontalFadingEdge
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.core.ui.component.icon.ArrowIconSpec
import com.kuit.afternote.core.ui.component.icon.RightArrowIcon
import com.kuit.afternote.ui.theme.Spacing
import com.kuit.afternote.ui.theme.White

/**
 * 앨범 커버 데이터 (edit·view 공통).
 */
data class AlbumCover(
    val id: String,
    val imageUrl: String? = null,
    val title: String? = null
)

private val songCountTextStyle = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontFamily = Sansneo,
    fontWeight = FontWeight.Normal,
    color = Color(0xFF000000)
)

@Composable
private fun MemorialPlaylistSongCountRow(
    songCount: Int,
    showArrow: Boolean
) {
    if (showArrow) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "현재 ${songCount}개의 노래가 담겨 있습니다.", style = songCountTextStyle)
            RightArrowIcon(color = B1, size = 16.dp)
        }
    } else {
        Text(text = "현재 ${songCount}개의 노래가 담겨 있습니다.", style = songCountTextStyle)
    }
}

@Composable
private fun MemorialPlaylistAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = B3, shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "노래 추가하기",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
        RightArrowIcon(
            iconSpec = ArrowIconSpec(
                iconRes = com.kuit.afternote.R.drawable.ic_arrow_right_playlist,
                contentDescription = "추가"
            ),
            backgroundColor = B1,
            size = 12.dp
        )
    }
}

/**
 * 추모 플레이리스트 컴포넌트 (edit·view 공통, 오버로딩으로 모드 구분).
 *
 * - **Edit mode**: onAddSongClick를 넘기면 "노래 추가하기" 버튼이 보이고, 카드 상단에는 개수 텍스트만 표시.
 * - **View mode**: null이면 버튼 없음, 개수 텍스트 오른쪽에 화살표 아이콘 표시.
 *
 * 앨범 행: albumCovers를 사용. albumItemContent를 넘기면 해당 슬롯으로 그리며, null이면 각 앨범을 회색 박스로 표시.
 *
 * @param onAddSongClick null이면 view 모드(버튼·편집 UI 없음), non-null이면 edit 모드
 * @param albumItemContent 앨범 셀 커스텀; null이면 기본 회색 박스 (view/placeholder용)
 */
@Composable
fun MemorialPlaylist(
    modifier: Modifier = Modifier,
    label: String = "추모 플레이리스트",
    songCount: Int = 0,
    albumCovers: List<AlbumCover> = emptyList(),
    onAddSongClick: (() -> Unit)? = null,
    albumItemContent: (@Composable (album: AlbumCover, index: Int) -> Unit)? = null
) {
    val isEditMode = onAddSongClick != null
    Column(
        verticalArrangement = Arrangement.spacedBy(space = Spacing.m)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(size = 16.dp))
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            MemorialPlaylistSongCountRow(
                songCount = songCount,
                showArrow = !isEditMode
            )
            if (albumCovers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                MemorialPlaylistAlbumRow(
                    albumCovers = albumCovers,
                    albumItemContent = albumItemContent
                )
            }
            if (onAddSongClick != null) {
                MemorialPlaylistAddButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onAddSongClick
                )
            }
        }
    }
}

@Composable
private fun MemorialPlaylistAlbumRow(
    albumCovers: List<AlbumCover>,
    albumItemContent: (@Composable (album: AlbumCover, index: Int) -> Unit)?
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalFadingEdge(edgeWidth = 45.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(albumCovers) { index, album ->
            if (albumItemContent != null) {
                albumItemContent(album, index)
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Edit mode")
@Composable
private fun MemorialPlaylistEditPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            MemorialPlaylist(
                songCount = 4,
                albumCovers = listOf(
                    AlbumCover("1"),
                    AlbumCover("2"),
                    AlbumCover("3"),
                    AlbumCover("4")
                ),
                onAddSongClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "View mode")
@Composable
private fun MemorialPlaylistViewPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            MemorialPlaylist(
                songCount = 16,
                albumCovers = listOf(
                    AlbumCover("1"),
                    AlbumCover("2"),
                    AlbumCover("3"),
                    AlbumCover("4")
                ),
                onAddSongClick = null
            )
        }
    }
}
