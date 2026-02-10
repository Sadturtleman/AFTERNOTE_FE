package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceiverEmailVerifyRepository
import javax.inject.Inject

/**
 * [ReceiverEmailVerifyRepository] No-op 구현체.
 * API 연동 전까지 성공만 반환하며, 서버 구축 후 실제 API 호출로 교체 예정.
 */
class ReceiverEmailVerifyRepositoryImpl
    @Inject
    constructor() : ReceiverEmailVerifyRepository {

    override suspend fun sendEmailCode(email: String): Result<Unit> = Result.success(Unit)

    override suspend fun verifyEmailCode(email: String, code: String): Result<Unit> =
        Result.success(Unit)
}
