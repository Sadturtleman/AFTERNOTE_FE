package com.kuit.afternote.feature.receiverauth.presentation.viewmodel

import com.kuit.afternote.feature.receiverauth.domain.entity.ReceiverAuthVerifyResult
import com.kuit.afternote.feature.receiverauth.domain.usecase.VerifyReceiverAuthUseCase
import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/**
 * [VerifySelfViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class VerifySelfViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var verifyReceiverAuthUseCase: VerifyReceiverAuthUseCase
    private lateinit var viewModel: VerifySelfViewModel

    @Before
    fun setUp() {
        verifyReceiverAuthUseCase = mockk()
        viewModel = VerifySelfViewModel(verifyReceiverAuthUseCase)
    }

    @Test
    fun verifyMasterKey_whenBlank_setsVerifyErrorRequired() {
        viewModel.updateMasterKey("")
        viewModel.verifyMasterKey()

        assertTrue(viewModel.uiState.value.verifyError is VerifyErrorType.Required)
        assertEquals(VerifyStep.MASTER_KEY_AUTH, viewModel.uiState.value.currentStep)
    }

    @Test
    fun verifyMasterKey_whenSuccess_advancesToUploadPdfStep() = runTest {
        coEvery { verifyReceiverAuthUseCase(any()) } returns Result.success(
            ReceiverAuthVerifyResult(receiverId = 1L)
        )
        viewModel.updateMasterKey("valid-key")
        viewModel.verifyMasterKey()
        advanceUntilIdle()

        assertEquals(VerifyStep.UPLOAD_PDF_AUTH, viewModel.uiState.value.currentStep)
        assertNull(viewModel.uiState.value.verifyError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun verifyMasterKey_when400BadRequest_setsVerifyErrorAndKeepsStep() = runTest {
        coEvery { verifyReceiverAuthUseCase(any()) } returns Result.failure(
            com.kuit.afternote.data.remote.ApiException(status = 400, code = 400, message = "잘못된 인증번호입니다.")
        )
        viewModel.updateMasterKey("wrong-key")
        viewModel.verifyMasterKey()
        advanceUntilIdle()

        assertEquals(VerifyStep.MASTER_KEY_AUTH, viewModel.uiState.value.currentStep)
        assertTrue(viewModel.uiState.value.verifyError is VerifyErrorType.Server)
        assertEquals("잘못된 인증번호입니다.", (viewModel.uiState.value.verifyError as VerifyErrorType.Server).message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun verifyMasterKey_whenNetworkError_setsVerifyErrorNetwork() = runTest {
        coEvery { verifyReceiverAuthUseCase(any()) } returns Result.failure(
            IOException("Network unavailable")
        )
        viewModel.updateMasterKey("key")
        viewModel.verifyMasterKey()
        advanceUntilIdle()

        assertEquals(VerifyStep.MASTER_KEY_AUTH, viewModel.uiState.value.currentStep)
        assertTrue(viewModel.uiState.value.verifyError is VerifyErrorType.Network)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun updateMasterKey_clearsVerifyError() {
        viewModel.updateMasterKey("")
        viewModel.verifyMasterKey()
        assertTrue(viewModel.uiState.value.verifyError is VerifyErrorType.Required)

        viewModel.updateMasterKey("a")
        assertNull(viewModel.uiState.value.verifyError)
    }

    @Test
    fun goToPreviousStep_fromUploadPdf_returnsMasterKeyStep() = runTest {
        coEvery { verifyReceiverAuthUseCase(any()) } returns Result.success(
            ReceiverAuthVerifyResult(receiverId = 1L)
        )
        viewModel.updateMasterKey("key")
        viewModel.verifyMasterKey()
        advanceUntilIdle()

        assertEquals(VerifyStep.UPLOAD_PDF_AUTH, viewModel.uiState.value.currentStep)
        val previous = viewModel.goToPreviousStep()
        assertEquals(VerifyStep.MASTER_KEY_AUTH, previous)
        assertEquals(VerifyStep.MASTER_KEY_AUTH, viewModel.uiState.value.currentStep)
    }
}
