package com.kuit.afternote.feature.afternote.presentation.mapper

import com.kuit.afternote.feature.afternote.domain.model.SearchedSong
import com.kuit.afternote.presentation.uimodel.PlaylistSongDisplay

fun SearchedSong.toDisplay() =
    PlaylistSongDisplay(
        id = id,
        title = title,
        artist = artist,
    )
