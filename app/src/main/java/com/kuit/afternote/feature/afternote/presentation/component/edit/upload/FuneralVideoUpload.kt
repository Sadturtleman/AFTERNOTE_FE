package com.kuit.afternote.feature.afternote.presentation.component.edit.upload

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 장례식에 남길 영상 추가 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 라벨과 버튼 간 간격: 6dp
 * - 큰 원형 버튼: B3 (#BDE0FF), 120dp
 * - 플러스 아이콘: 중앙에 위치, 24dp
 */
@Composable
fun FuneralVideoUpload(
    modifier: Modifier = Modifier,
    label: String = "장례식에 남길 영상",
    videoUrl: String? = null,
    onAddVideoClick: () -> Unit,
    onThumbnailBytesReady: (ByteArray?) -> Unit = {},
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val hasVideo = !videoUrl.isNullOrBlank()
    val addContentDescription = if (hasVideo) "영상 변경" else "영상 추가"
    val context = LocalContext.current
    var thumbnailBitmap by remember(videoUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(videoUrl) {
        if (videoUrl.isNullOrBlank()) {
            thumbnailBitmap = null
            onThumbnailBytesReady(null)
            return@LaunchedEffect
        }
        val (bitmap, jpegBytes) = withContext(ioDispatcher) {
            val bmp = runCatching {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(context, videoUrl.toUri())
                    retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                } finally {
                    retriever.release()
                }
            }.getOrNull()
            val bytes = bmp?.let { frame ->
                ByteArrayOutputStream().use { out ->
                    frame.compress(Bitmap.CompressFormat.JPEG, 85, out)
                    out.toByteArray()
                }
            }
            Pair(bmp, bytes)
        }
        thumbnailBitmap = bitmap?.asImageBitmap()
        runCatching { onThumbnailBytesReady(jpegBytes) }.onFailure { /* avoid throwing from LaunchedEffect */ }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
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

        if (!hasVideo) {
            // 업로드 전 상태: 흰색 카드 + 플러스 아이콘
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(color = White, shape = RoundedCornerShape(size = 16.dp))
                    .clickable(onClick = onAddVideoClick)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_add_circle),
                        contentDescription = addContentDescription,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            // 업로드 후 상태: 썸네일 또는 그라데이션 배경 + 재생 아이콘
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(size = 16.dp))
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color(0x99757575),
                                        Color(0x99222222)
                                    )
                            )
                    )
                    .clickable(onClick = onAddVideoClick)
            ) {
                if (thumbnailBitmap != null) {
                    Image(
                        bitmap = thumbnailBitmap!!,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                // 중앙 재생 아이콘 (썸네일 위 또는 그라데이션 위)
                Image(
                    painter = painterResource(R.drawable.ic_playback),
                    contentDescription = stringResource(R.string.content_description_video_play),
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FuneralVideoUploadPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 영상 없음
            FuneralVideoUpload(
                onAddVideoClick = {}
            )

            // 영상 있음 (실기기에서 선택 시 썸네일 표시)
            FuneralVideoUpload(
                videoUrl = "test",
                onAddVideoClick = {}
            )
        }
    }
}
