package com.kuit.afternote.feature.receiverauth.presentation.viewmodel

import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep
import kotlinx.coroutines.flow.StateFlow

/**
 * VerifySelf 화면에서 사용하는 ViewModel 계약.
 *
 * Preview에서 Hilt 없이 Fake로 주입할 수 있도록 인터페이스로 정의합니다.
 */
interface VerifySelfViewModelContract {

    val uiState: StateFlow<VerifySelfUiState>

    fun updateMasterKey(text: String)
    fun verifyMasterKey()
    fun goToNextStep()
    fun goToPreviousStep(): VerifyStep?
    fun clearVerifyError()
}
