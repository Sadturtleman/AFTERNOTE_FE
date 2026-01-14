package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.horizontalFadingEdge
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.Spacing

/**
 * 앨범 커버 데이터
 */
data class AlbumCover(
    val id: String,
    val imageUrl: String? = null,
    val title: String? = null
)

/**
 * 추모 플레이리스트 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 16sp, Medium, Gray9
 * - 라벨과 콘텐츠 간 간격: 6dp
 * - 노래 개수 텍스트: 14sp, Regular, Gray6
 * - 앨범 커버: 64dp x 64dp, 8dp 모서리
 * - 앨범 커버 간 간격: 8dp
 * - 추가 버튼: B3 배경, 8dp 모서리, "노래 추가하기" 텍스트 + 화살표 아이콘
 */
@Composable
fun MemorialPlaylist(
    modifier: Modifier = Modifier,
    label: String = "추모 플레이리스트",
    songCount: Int = 0,
    albumCovers: List<AlbumCover> = emptyList(),
    onAddSongClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            // 노래 개수 텍스트
            Text(
                text = "현재 ${songCount}개의 노래가 담겨 있습니다.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF000000)
                )
            )

            // 앨범 커버 리스트 (페이드 아웃 효과 포함)
            if (albumCovers.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalFadingEdge(
                            edgeWidth = 45.dp,
                            edgeColor = Gray1
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(albumCovers) { album ->
                        Box(
                            modifier = Modifier
                                .size(87.dp)
                                .background(color = Color.Black)
                        ) {
                            if (album.imageUrl != null) {
                                // TODO: 실제 이미지 로드 (Coil 등 사용)
                            }
                        }
                    }
                }
            }
        }

        // 노래 추가하기 버튼
        Row(
            modifier = Modifier
                .background(
                    color = B3,
                    shape = RoundedCornerShape(20.dp)
                ).clickable(onClick = onAddSongClick)
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .align(Alignment.End),
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

            // 밝은 파란색 원형 배경에 흰색 화살표 아이콘
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(B1)
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_right_vector),
                    contentDescription = "추가",
//                        modifier = Modifier
//                            .height(height = 6.dp)
//                            .width(width = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialPlaylistPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            // 빈 플레이리스트
            MemorialPlaylist(
                songCount = 0,
                albumCovers = emptyList(),
                onAddSongClick = {}
            )

            // 노래가 있는 플레이리스트
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
