package com.kuit.afternote.feature.receiverauth.presentation.viewmodel

import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus
import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep

/**
 * 수신자 본인 인증(VerifySelf) 화면 UI 상태.
 *
 * @param currentStep 현재 단계 (마스터키 입력 / PDF 업로드 / 완료)
 * @param masterKeyInput 마스터 키 입력값
 * @param isLoading verify API 호출 중 여부
 * @param verifyError 검증 실패 시 에러 타입 (화면에서 stringResource로 메시지 표시)
 * @param verifiedReceiverId 마스터키 검증 성공 시 수신받은 receiverId (열람요청 완료에서 수신자 홈 이동 시 사용)
 * @param verifiedSenderName 마스터키 검증 성공 시 수신받은 발신자 이름 (수신자 홈 상단 "故 OO님이 남기신 기록" 표시용)
 * @param isSubmitting 증빙 서류 업로드·제출 중 여부
 * @param submitError 증빙 서류 제출 실패 시 에러 타입
 * @param deliveryVerificationStatus END 단계에서 표시할 인증 요청 상태 (PENDING/APPROVED/REJECTED)
 */
data class VerifySelfUiState(
    val currentStep: VerifyStep = VerifyStep.MASTER_KEY_AUTH,
    val masterKeyInput: String = "",
    val isLoading: Boolean = false,
    val verifyError: VerifyErrorType? = null,
    val verifiedReceiverId: Long? = null,
    val verifiedSenderName: String? = null,
    val isSubmitting: Boolean = false,
    val submitError: VerifyErrorType? = null,
    val deliveryVerificationStatus: DeliveryVerificationStatus? = null
)

/**
 * 마스터 키 검증 실패 시 에러 타입.
 *
 * 화면에서 [stringResource]로 메시지를 표시합니다.
 */
sealed class VerifyErrorType {
    /** 마스터 키 미입력 */
    data object Required : VerifyErrorType()

    /** 네트워크 오류 */
    data object Network : VerifyErrorType()

    /** 서버 에러 (400 등). [message]는 서버에서 내려준 메시지. */
    data class Server(val message: String) : VerifyErrorType()

    /** 알 수 없는 오류 (화면에서 receiver_verify_error_unknown 사용) */
    data object Unknown : VerifyErrorType()
}
