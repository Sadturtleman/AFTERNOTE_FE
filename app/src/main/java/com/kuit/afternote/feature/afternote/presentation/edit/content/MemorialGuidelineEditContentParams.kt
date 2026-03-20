package com.kuit.afternote.feature.afternote.presentation.edit.content

import androidx.compose.runtime.Immutable
import com.kuit.afternote.core.component.LastWishOption
import com.kuit.afternote.core.component.list.AlbumCover
import com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiverSection

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
    val funeralThumbnailUrl: String? = null,
    val customLastWishText: String,
    val recipientSection: AfternoteEditReceiverSection? = null,
    val onSongAddClick: () -> Unit,
    val onLastWishSelected: (String) -> Unit,
    val onCustomLastWishChanged: (String) -> Unit,
    val onPhotoAddClick: () -> Unit,
    val onVideoAddClick: () -> Unit,
    val onThumbnailBytesReady: (ByteArray?) -> Unit = {},
)
