package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreatePlaylistRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiverDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteIdResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItemDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideoDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSongDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [AfternoteRepositoryImpl] unit tests.
 *
 * These tests exercise all repository methods so that the Afternote API
 * entrypoints are actually used and verified against the Retrofit service.
 */
class AfternoteRepositoryImplTest {

    private lateinit var api: AfternoteApiService
    private lateinit var repository: AfternoteRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = AfternoteRepositoryImpl(api)
    }

    @Test
    fun getAfternotes_whenSuccess_returnsMappedItems() =
        runTest {
            val listDto =
                AfternoteListResponseDto(
                    content =
                        listOf(
                            AfternoteListItemDto(
                                afternoteId = 10L,
                                title = "인스타그램",
                                category = "SOCIAL",
                                createdAt = "2025-11-26T14:30:00"
                            )
                        ),
                    page = 0,
                    size = 10,
                    hasNext = false
                )
            coEvery { api.getAfternotes(category = null, page = 0, size = 10) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = listDto)

            val result = repository.getAfternotes(category = null, page = 0, size = 10)

            assertTrue(result.isSuccess)
            val paged = result.getOrNull()
            assertEquals(1, paged?.items?.size)
            assertEquals("인스타그램", paged?.items?.first()?.serviceName)
            assertTrue(paged?.hasNext == false)
            coVerify(exactly = 1) { api.getAfternotes(category = null, page = 0, size = 10) }
        }

    @Test
    fun createSocial_whenSuccess_returnsId() =
        runTest {
            coEvery { api.createAfternoteSocial(any()) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponseDto(afternoteId = 3L))

            val result =
                repository.createSocial(
                    title = "인스타그램",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    credentialsId = "id",
                    credentialsPassword = "pw"
                )

            assertTrue(result.isSuccess)
            assertEquals(3L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternoteSocial(
                    AfternoteCreateSocialRequestDto(
                        category = "SOCIAL",
                        title = "인스타그램",
                        processMethod = "MEMORIAL",
                        actions = listOf("게시물 내리기"),
                        leaveMessage = "감사했습니다",
                        credentials = AfternoteCredentialsDto(id = "id", password = "pw")
                    )
                )
            }
        }

    @Test
    fun createGallery_whenSuccess_returnsId() =
        runTest {
            coEvery { api.createAfternoteGallery(any()) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponseDto(afternoteId = 5L))

            val result =
                repository.createGallery(
                    title = "가족 사진",
                    processMethod = "DELETE",
                    actions = listOf("사진 백업"),
                    leaveMessage = "소중한 추억들",
                    receiverIds = listOf(1L, 2L)
                )

            assertTrue(result.isSuccess)
            assertEquals(5L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternoteGallery(
                    AfternoteCreateGalleryRequestDto(
                        category = "GALLERY",
                        title = "가족 사진",
                        processMethod = "DELETE",
                        actions = listOf("사진 백업"),
                        leaveMessage = "소중한 추억들",
                        receivers =
                            listOf(
                                AfternoteReceiverRefDto(receiverId = 1L),
                                AfternoteReceiverRefDto(receiverId = 2L)
                            )
                    )
                )
            }
        }

    @Test
    fun getAfternoteDetail_whenSuccess_returnsDetail() =
        runTest {
            val detail =
                AfternoteDetailResponseDto(
                    afternoteId = 10L,
                    category = "SOCIAL",
                    title = "인스타그램",
                    createdAt = "2025-11-26T14:30:00",
                    updatedAt = "2025-11-26T14:30:00",
                    credentials = AfternoteCredentialsDto(id = "id", password = "pw"),
                    receivers =
                        listOf(
                            AfternoteDetailReceiverDto(
                                name = "수신인",
                                relation = "친구",
                                phone = "010-0000-0000"
                            )
                        ),
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    playlist = null
                )
            coEvery { api.getAfternoteDetail(afternoteId = 10L) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = detail)

            val result = repository.getAfternoteDetail(afternoteId = 10L)

            assertTrue(result.isSuccess)
            val detailDomain = result.getOrNull()
            assertEquals(10L, detailDomain?.id)
            coVerify(exactly = 1) { api.getAfternoteDetail(afternoteId = 10L) }
        }

    @Test
    fun createPlaylist_whenSuccess_returnsId() =
        runTest {
            coEvery { api.createAfternotePlaylist(any()) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponseDto(afternoteId = 7L))

            val playlist =
                AfternotePlaylistDto(
                    profilePhoto = null,
                    atmosphere = "차분하게",
                    songs =
                        listOf(
                            AfternoteSongDto(
                                id = null,
                                title = "보고싶다",
                                artist = "김범수",
                                coverUrl = "https://example.com"
                            )
                        ),
                    memorialVideo =
                        AfternoteMemorialVideoDto(
                            videoUrl = "https://video",
                            thumbnailUrl = "https://thumb"
                        )
                )

            val result = repository.createPlaylist(title = "마지막 플레이리스트", playlist = playlist)

            assertTrue(result.isSuccess)
            assertEquals(7L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternotePlaylist(
                    AfternoteCreatePlaylistRequestDto(
                        category = "PLAYLIST",
                        title = "마지막 플레이리스트",
                        playlist = playlist
                    )
                )
            }
        }

    @Test
    fun updateAfternote_whenSuccess_returnsId() =
        runTest {
            coEvery { api.updateAfternote(any(), any()) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponseDto(afternoteId = 10L))

            val body =
                AfternoteUpdateRequestDto(
                    category = "SOCIAL",
                    title = "수정된 제목",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 유지"),
                    leaveMessage = "수정된 메시지",
                    credentials = AfternoteCredentialsDto(id = null, password = "new_pw"),
                    receivers = null,
                    playlist = null
                )

            val result = repository.updateAfternote(afternoteId = 10L, body = body)

            assertTrue(result.isSuccess)
            assertEquals(10L, result.getOrNull())
            coVerify(exactly = 1) { api.updateAfternote(afternoteId = 10L, body = body) }
        }

    @Test
    fun deleteAfternote_whenSuccess_returnsUnit() =
        runTest {
            coEvery { api.deleteAfternote(afternoteId = 10L) } returns
                ApiResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponseDto(afternoteId = 10L))

            val result = repository.deleteAfternote(afternoteId = 10L)

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { api.deleteAfternote(afternoteId = 10L) }
        }
}

