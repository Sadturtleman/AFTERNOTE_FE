package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    params: MemorialGuidelineEditContentParams
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

        // 추모 가이드라인 탭 하단 여백 (272dp)
        Spacer(modifier = Modifier.height(Spacing.memorialGuidelineBottom))
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
