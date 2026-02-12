package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSongDto
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreatePlaylistAfternoteUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: CreatePlaylistAfternoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreatePlaylistAfternoteUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsId() =
        runTest {
            val playlist =
                AfternotePlaylistDto(
                    profilePhoto = null,
                    atmosphere = "차분하게",
                    songs = listOf(AfternoteSongDto(id = null, title = "보고싶다", artist = "김범수", coverUrl = "https://example.com")),
                    memorialVideo = null
                )
            coEvery {
                repository.createPlaylist(
                    title = any(),
                    playlist = any()
                )
            } returns Result.success(7L)

            val result = useCase(title = "마지막 플레이리스트", playlist = playlist)

            assertTrue(result.isSuccess)
            assertEquals(7L, result.getOrNull())
            coVerify(exactly = 1) {
                repository.createPlaylist(
                    title = "마지막 플레이리스트",
                    playlist = playlist
                )
            }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            val playlist =
                AfternotePlaylistDto(
                    profilePhoto = null,
                    atmosphere = null,
                    songs = emptyList(),
                    memorialVideo = null
                )
            coEvery {
                repository.createPlaylist(
                    title = any(),
                    playlist = any()
                )
            } returns Result.failure(RuntimeException("Validation failed"))

            val result = useCase(title = "제목", playlist = playlist)

            assertTrue(result.isFailure)
            assertEquals("Validation failed", result.exceptionOrNull()?.message)
        }
}
