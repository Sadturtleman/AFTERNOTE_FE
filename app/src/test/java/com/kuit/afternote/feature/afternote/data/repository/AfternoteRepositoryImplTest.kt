package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.core.data.model.BaseResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequest
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteIdResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideo
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.data.service.AfternoteApiService
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
                AfternoteListResponse(
                    content =
                        listOf(
                            AfternoteListItem(
                                afternoteId = 10L,
                                title = "인스타그램",
                                category = "SOCIAL",
                                createdAt = "2025-11-26T14:30:00",
                            ),
                        ),
                    page = 0,
                    size = 10,
                    hasNext = false,
                )
            coEvery { api.getAfternotes(category = null, page = 0, size = 10) } returns
                BaseResponse(status = 200, code = 0, message = "OK", data = listDto)

            val result = repository.getAfternotes()

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
                BaseResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponse(afternoteId = 3L))

            val result =
                repository.createSocial(
                    title = "인스타그램",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    credentialsId = "id",
                    credentialsPassword = "pw",
                )

            assertTrue(result.isSuccess)
            assertEquals(3L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternoteSocial(
                    AfternoteCreateSocialRequest(
                        category = "SOCIAL",
                        title = "인스타그램",
                        processMethod = "MEMORIAL",
                        actions = listOf("게시물 내리기"),
                        leaveMessage = "감사했습니다",
                        credentials = AfternoteCredentials(id = "id", password = "pw"),
                        receivers = emptyList(),
                    ),
                )
            }
        }

    @Test
    fun createGallery_whenSuccess_returnsId() =
        runTest {
            coEvery { api.createAfternoteGallery(any()) } returns
                BaseResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponse(afternoteId = 5L))

            val result =
                repository.createGallery(
                    title = "가족 사진",
                    processMethod = "DELETE",
                    actions = listOf("사진 백업"),
                    leaveMessage = "소중한 추억들",
                    receiverIds = listOf(1L, 2L),
                )

            assertTrue(result.isSuccess)
            assertEquals(5L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternoteGallery(
                    AfternoteCreateGalleryRequest(
                        category = "GALLERY",
                        title = "가족 사진",
                        processMethod = "DELETE",
                        actions = listOf("사진 백업"),
                        leaveMessage = "소중한 추억들",
                        receivers =
                            listOf(
                                AfternoteReceiverRef(receiverId = 1L),
                                AfternoteReceiverRef(receiverId = 2L),
                            ),
                    ),
                )
            }
        }

    @Test
    fun getAfternoteDetail_whenSuccess_returnsDetail() =
        runTest {
            val detail =
                AfternoteDetailResponse(
                    afternoteId = 10L,
                    category = "SOCIAL",
                    title = "인스타그램",
                    createdAt = "2025-11-26T14:30:00",
                    updatedAt = "2025-11-26T14:30:00",
                    credentials = AfternoteCredentials(id = "id", password = "pw"),
                    receivers =
                        listOf(
                            AfternoteDetailReceiver(
                                name = "수신인",
                                relation = "친구",
                                phone = "010-0000-0000",
                            ),
                        ),
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    playlist = null,
                )
            coEvery { api.getAfternoteDetail(afternoteId = 10L) } returns
                BaseResponse(status = 200, code = 0, message = "OK", data = detail)

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
                BaseResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponse(afternoteId = 7L))

            val playlist =
                AfternotePlaylist(
                    profilePhoto = null,
                    atmosphere = "차분하게",
                    songs =
                        listOf(
                            AfternoteSong(
                                id = null,
                                title = "보고싶다",
                                artist = "김범수",
                                coverUrl = "https://example.com",
                            ),
                        ),
                    memorialVideo =
                        AfternoteMemorialVideo(
                            videoUrl = "https://video",
                            thumbnailUrl = "https://thumb",
                        ),
                )

            val result =
                repository.createPlaylist()

            assertTrue(result.isSuccess)
            assertEquals(7L, result.getOrNull())
            coVerify(exactly = 1) {
                api.createAfternotePlaylist(
                    AfternoteCreatePlaylistRequest(
                        category = "PLAYLIST",
                        title = "마지막 플레이리스트",
                        playlist = playlist,
                        receivers = emptyList(),
                    ),
                )
            }
        }

    @Test
    fun updateAfternote_whenSuccess_returnsId() =
        runTest {
            coEvery { api.updateAfternote(any(), any()) } returns
                BaseResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponse(afternoteId = 10L))

            val body =
                AfternoteUpdateRequest(
                    category = "SOCIAL",
                    title = "수정된 제목",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 유지"),
                    leaveMessage = "수정된 메시지",
                    credentials = AfternoteCredentials(id = null, password = "new_pw"),
                    receivers = null,
                    playlist = null,
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
                BaseResponse(status = 200, code = 0, message = "OK", data = AfternoteIdResponse(afternoteId = 10L))

            val result = repository.deleteAfternote(afternoteId = 10L)

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { api.deleteAfternote(afternoteId = 10L) }
        }
}
