package com.kuit.afternote.feature.afternote.presentation.component.edit.content

import androidx.compose.runtime.Immutable
import com.kuit.afternote.core.ui.component.LastWishOption
import com.kuit.afternote.core.ui.component.list.AlbumCover

/**
 * 추모 가이드라인 콘텐츠 파라미터
 */
@Immutable
data class MemorialGuidelineEditContentParams(
    val displayMemorialPhotoUri: String?,
    val playlistSongCount: Int,
    val playlistAlbumCovers: List<AlbumCover>,
    val selectedLastWish: String?,
    val lastWishOptions: List<LastWishOption>,
    val funeralVideoUrl: String?,
    val customLastWishText: String,
    val onSongAddClick: () -> Unit,
    val onLastWishSelected: (String) -> Unit,
    val onCustomLastWishChanged: (String) -> Unit,
    val onPhotoAddClick: () -> Unit,
    val onVideoAddClick: () -> Unit
)
