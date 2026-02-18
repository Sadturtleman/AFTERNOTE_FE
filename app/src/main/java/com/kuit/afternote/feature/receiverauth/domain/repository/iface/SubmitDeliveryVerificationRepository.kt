package com.kuit.afternote.feature.receiverauth.domain.repository.iface

/**
 * 수신자 사망확인 서류 제출용 Domain Repository.
 *
 * POST /api/receiver-auth/delivery-verification 호출을 래핑합니다.
 */
interface SubmitDeliveryVerificationRepository {

    /**
     * 사망진단서·가족관계증명서 URL을 서버에 제출합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code)
     * @param deathCertificateUrl 사망진단서 업로드 완료 fileUrl
     * @param familyRelationCertificateUrl 가족관계증명서 업로드 완료 fileUrl
     * @return 성공 시 [Result.success], 실패 시 [Result.failure]
     */
    suspend fun submit(
        authCode: String,
        deathCertificateUrl: String,
        familyRelationCertificateUrl: String
    ): Result<Unit>
}
