package com.kuit.afternote.feature.receiver.domain.repository.iface

/**
 * 수신자 이메일 인증(발송/검증) Repository 인터페이스.
 * API 연동 전까지 No-op 구현 사용, 서버 구축 후 실제 API 호출로 교체.
 */
interface ReceiverEmailVerifyRepository {

    /**
     * 수신자 이메일로 인증번호 발송을 요청합니다.
     *
     * @param email 발송 대상 이메일 주소
     * @return 성공 시 [Result.success], 실패 시 [Result.failure]
     */
    suspend fun sendEmailCode(email: String): Result<Unit>

    /**
     * 이메일과 인증번호로 본인 확인을 검증합니다.
     *
     * @param email 이메일 주소
     * @param code 인증번호
     * @return 성공 시 [Result.success], 실패 시 [Result.failure]
     */
    suspend fun verifyEmailCode(email: String, code: String): Result<Unit>
}
