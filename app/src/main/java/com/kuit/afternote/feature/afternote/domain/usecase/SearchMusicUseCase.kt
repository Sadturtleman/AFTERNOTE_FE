package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.afternote.domain.repository.iface.MusicSearchRepository
import javax.inject.Inject

/**
 * 음악 검색 UseCase.
 *
 * GET /music/search?keyword= — keyword가 비어 있으면 빈 목록 반환 (API 400 방지).
 */
class SearchMusicUseCase
@Inject
constructor(
    private val repository: MusicSearchRepository
) {

    /**
     * @param keyword 검색할 가수명 또는 노래 제목 (blank면 빈 목록 반환)
     */
    suspend operator fun invoke(keyword: String): Result<List<PlaylistSongDisplay>> =
        repository.search(keyword)
}
