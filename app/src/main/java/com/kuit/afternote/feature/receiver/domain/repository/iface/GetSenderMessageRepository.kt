package com.kuit.afternote.feature.receiver.domain.repository.iface

/**
 * 수신자 인증번호(authCode)로 발신자가 남긴 메시지를 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/message (X-Auth-Code) API를 사용합니다.
 */
interface GetSenderMessageRepository {

    /**
     * 인증번호로 발신자 메시지를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (마스터키, X-Auth-Code 헤더)
     * @return 발신자 메시지 (nullable)
     */
    suspend fun getMessage(authCode: String): Result<String?>
}
