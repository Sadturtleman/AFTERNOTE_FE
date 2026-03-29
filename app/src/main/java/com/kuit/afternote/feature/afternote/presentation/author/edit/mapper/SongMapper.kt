package com.kuit.afternote.feature.afternote.presentation.author.edit.mapper
import com.kuit.afternote.feature.afternote.domain.model.SearchedSong
import com.kuit.afternote.feature.afternote.presentation.shared.uimodel.PlaylistSongDisplay

fun SearchedSong.toDisplay() =
    PlaylistSongDisplay(
        id = id,
        title = title,
        artist = artist,
    )
