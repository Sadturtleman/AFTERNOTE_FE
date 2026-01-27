package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.AlbumCover
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.LastMomentQuestion
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.LastWishOption
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.LastWishesRadioGroup
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.MemorialPlaylist
import com.kuit.afternote.feature.mainpage.presentation.component.edit.upload.FuneralVideoUpload
import com.kuit.afternote.feature.mainpage.presentation.component.edit.upload.MemorialPhotoUpload
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Spacing

/**
 * 추모 가이드라인 종류 선택 시 표시되는 콘텐츠
 */
@Composable
fun MemorialGuidelineEditContent(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    params: MemorialGuidelineEditContentParams
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    // Scaffold가 제공하는 bottomPadding을 사용 (네비게이션 바 높이 + 시스템 바 높이 자동 계산)
    val bottomPaddingDp = bottomPadding.calculateBottomPadding()
    // Viewport 높이 = 창 높이 - bottomPadding (네비게이션 바 상단까지의 높이)
    // 하단 여백은 네비게이션 바 상단까지의 Viewport 높이의 10%로 계산
    val viewportHeight = with(density) {
        windowInfo.containerSize.height.toDp() - bottomPaddingDp
    }
    val spacerHeight = viewportHeight * 0.1f

    MemorialGuidelineEditContentContent(
        modifier = modifier,
        params = params,
        spacerHeight = spacerHeight
    )
}

@Composable
private fun MemorialGuidelineEditContentContent(
    modifier: Modifier = Modifier,
    params: MemorialGuidelineEditContentParams,
    spacerHeight: Dp
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 마지막 순간 질문
        LastMomentQuestion()

        Spacer(modifier = Modifier.height(Spacing.xl))

        // 영정사진 추가
        MemorialPhotoUpload(
            imageUrl = params.memorialPhotoUrl,
            onAddPhotoClick = params.onPhotoAddClick
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        // 추모 플레이리스트
        MemorialPlaylist(
            songCount = params.playlistSongCount,
            albumCovers = params.playlistAlbumCovers,
            onAddSongClick = params.onSongAddClick
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        // 남기고 싶은 당부
        LastWishesRadioGroup(
            options = params.lastWishOptions,
            selectedValue = params.selectedLastWish,
            onOptionSelected = params.onLastWishSelected
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        // 장례식에 남길 영상
        FuneralVideoUpload(
            videoUrl = params.funeralVideoUrl,
            onAddVideoClick = params.onVideoAddClick
        )

        // 추모 가이드라인 탭 하단 여백 (Viewport 높이의 10%, 800dp 기준 약 80dp)
        // LocalWindowInfo를 사용하여 창 높이를 기준으로 계산
        Spacer(modifier = Modifier.height(spacerHeight))
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialGuidelineEditContentPreview() {
    AfternoteTheme {
        val lastWishOptions = listOf(
            LastWishOption(
                text = "차분하고 조용하게 보내주세요.",
                value = "calm"
            ),
            LastWishOption(
                text = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요.",
                value = "bright"
            ),
            LastWishOption(
                text = "기타(직접 입력)",
                value = "other"
            )
        )

        MemorialGuidelineEditContent(
            bottomPadding = PaddingValues(bottom = 88.dp),
            params = MemorialGuidelineEditContentParams(
                memorialPhotoUrl = null,
                playlistSongCount = 16,
                playlistAlbumCovers = listOf(
                    AlbumCover("1"),
                    AlbumCover("2"),
                    AlbumCover("3"),
                    AlbumCover("4")
                ),
                selectedLastWish = "calm",
                lastWishOptions = lastWishOptions,
                funeralVideoUrl = null,
                onPhotoAddClick = {},
                onSongAddClick = {},
                onLastWishSelected = {},
                onVideoAddClick = {}
            )
        )
    }
}
