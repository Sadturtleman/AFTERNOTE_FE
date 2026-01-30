package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.AlbumCover
import com.kuit.afternote.core.ui.component.LastWishOption
import com.kuit.afternote.core.ui.component.LastWishesRadioGroup
import com.kuit.afternote.core.ui.component.MemorialGuidelineContent
import com.kuit.afternote.core.ui.component.MemorialGuidelineSlots
import com.kuit.afternote.core.ui.component.MemorialPlaylist
import com.kuit.afternote.feature.mainpage.presentation.common.util.PlaceholderDrawables
import com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial.LastMomentQuestion
import com.kuit.afternote.feature.mainpage.presentation.component.edit.upload.FuneralVideoUpload
import com.kuit.afternote.feature.mainpage.presentation.component.edit.upload.MemorialPhotoUpload
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 추모 가이드라인 종류 선택 시 표시되는 콘텐츠 (편집 모드).
 * [MemorialGuidelineContent] 공통 레이아웃을 사용하고, 슬롯에 편집용 컴포넌트를 채움.
 */
@Composable
fun MemorialGuidelineEditContent(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    params: MemorialGuidelineEditContentParams
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val bottomPaddingDp = bottomPadding.calculateBottomPadding()
    val viewportHeight = with(density) {
        windowInfo.containerSize.height.toDp() - bottomPaddingDp
    }
    val trailingSpacerHeight = viewportHeight * 0.1f

    MemorialGuidelineContent(
        modifier = modifier,
        slots = MemorialGuidelineSlots(
            introContent = { LastMomentQuestion() },
            photoContent = {
                MemorialPhotoUpload(
                    imageUrl = params.memorialPhotoUrl,
                    onAddPhotoClick = params.onPhotoAddClick
                )
            },
            playlistContent = {
                MemorialPlaylist(
                    songCount = params.playlistSongCount,
                    albumCovers = params.playlistAlbumCovers,
                    onAddSongClick = params.onSongAddClick,
                    albumItemContent = { _, index ->
                        Box(modifier = Modifier.size(80.dp)) {
                            Image(
                                painter = painterResource(
                                    id = PlaceholderDrawables.forZeroBasedIndex(index)
                                ),
                                contentDescription = "${index + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                )
            },
            lastWishContent = {
                LastWishesRadioGroup(
                    options = params.lastWishOptions,
                    selectedValue = params.selectedLastWish,
                    onOptionSelected = params.onLastWishSelected
                )
            },
            videoContent = {
                FuneralVideoUpload(
                    videoUrl = params.funeralVideoUrl,
                    onAddVideoClick = params.onVideoAddClick
                )
            }
        ),
        sectionSpacing = 32.dp,
        trailingSpacerHeight = trailingSpacerHeight
    )
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
