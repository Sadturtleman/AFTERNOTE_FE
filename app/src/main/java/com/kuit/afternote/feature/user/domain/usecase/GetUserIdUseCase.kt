package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.data.local.TokenManager
import javax.inject.Inject

/**
 * 사용자 ID 조회 UseCase.
 *
 * JWT 토큰에서 userId를 추출하여 반환합니다.
 * ViewModel이 TokenManager를 직접 접근하지 않도록 Domain Layer를 통해 제공합니다.
 */
class GetUserIdUseCase
    @Inject
    constructor(
        private val tokenManager: TokenManager
    ) {
        /**
         * JWT 토큰에서 userId를 추출합니다.
         *
         * @return userId (Long) 또는 null (토큰이 없거나 userId를 찾을 수 없는 경우)
         */
        suspend operator fun invoke(): Long? = tokenManager.getUserId()
    }
