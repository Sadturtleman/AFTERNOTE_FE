package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteProcessingMethod
import com.kuit.afternote.feature.afternote.domain.usecase.CreateGalleryAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreatePlaylistAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreateSocialAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UpdateAfternoteUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AfternoteEditViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var createSocialUseCase: CreateSocialAfternoteUseCase
    private lateinit var createGalleryUseCase: CreateGalleryAfternoteUseCase
    private lateinit var createPlaylistUseCase: CreatePlaylistAfternoteUseCase
    private lateinit var updateUseCase: UpdateAfternoteUseCase
    private lateinit var viewModel: AfternoteEditViewModel

    private val socialPayload =
        RegisterAfternotePayload(
            serviceName = "인스타그램",
            date = "2025.11.26",
            accountId = "id",
            password = "pw",
            message = "감사했습니다",
            accountProcessingMethod = "MEMORIAL",
            informationProcessingMethod = "",
            processingMethods = listOf(AfternoteProcessingMethod("1", "게시물 내리기")),
            galleryProcessingMethods = emptyList()
        )

    @Before
    fun setUp() {
        createSocialUseCase = mockk()
        createGalleryUseCase = mockk()
        createPlaylistUseCase = mockk()
        updateUseCase = mockk()
        viewModel =
            AfternoteEditViewModel(
                createSocialUseCase = createSocialUseCase,
                createGalleryUseCase = createGalleryUseCase,
                createPlaylistUseCase = createPlaylistUseCase,
                updateUseCase = updateUseCase
            )
    }

    @Test
    fun saveAfternote_whenCreateSocialSuccess_setsSaveSuccess() =
        runTest {
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.success(3L)

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertTrue(viewModel.saveState.value.saveSuccess)
            assertEquals(3L, viewModel.saveState.value.savedId)
            assertTrue(viewModel.saveState.value.error == null)
        }

    @Test
    fun saveAfternote_whenUpdateSuccess_setsSaveSuccess() =
        runTest {
            coEvery {
                updateUseCase(
                    afternoteId = any(),
                    body = any()
                )
            } returns Result.success(10L)

            viewModel.saveAfternote(
                editingId = 10L,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertTrue(viewModel.saveState.value.saveSuccess)
            assertEquals(10L, viewModel.saveState.value.savedId)
        }

    @Test
    fun saveAfternote_whenCreateSocial400BadRequest_setsError() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Bad request"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Long>(400, errorBody))
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.failure(httpException)

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertFalse(viewModel.saveState.value.saveSuccess)
            assertTrue(viewModel.saveState.value.error?.contains("400") == true)
        }

    @Test
    fun saveAfternote_whenCreateSocial401Unauthorized_setsError() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Long>(401, errorBody))
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.failure(httpException)

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertFalse(viewModel.saveState.value.saveSuccess)
            assertTrue(viewModel.saveState.value.error?.contains("401") == true)
        }

    @Test
    fun saveAfternote_whenCreateSocial404NotFound_setsError() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Long>(404, errorBody))
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.failure(httpException)

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertFalse(viewModel.saveState.value.saveSuccess)
            assertTrue(viewModel.saveState.value.error?.contains("404") == true)
        }

    @Test
    fun saveAfternote_whenCreateSocial500ServerError_setsError() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Long>(500, errorBody))
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.failure(httpException)

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertFalse(viewModel.saveState.value.saveSuccess)
            assertTrue(viewModel.saveState.value.error?.contains("500") == true)
        }

    @Test
    fun saveAfternote_whenCreateSocialNetworkError_setsError() =
        runTest {
            coEvery {
                createSocialUseCase(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any()
                )
            } returns Result.failure(java.io.IOException("Network unavailable"))

            viewModel.saveAfternote(
                editingId = null,
                category = "소셜네트워크",
                payload = socialPayload,
                receivers = emptyList(),
                playlistStateHolder = null
            )
            advanceUntilIdle()

            assertFalse(viewModel.saveState.value.isSaving)
            assertFalse(viewModel.saveState.value.saveSuccess)
            assertEquals("Network unavailable", viewModel.saveState.value.error)
        }
}
