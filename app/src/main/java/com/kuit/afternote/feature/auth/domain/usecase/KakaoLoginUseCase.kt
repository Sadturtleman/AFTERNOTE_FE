package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 카카오 로그인 UseCase.
 *
 * Kakao SDK로 발급받은 accessToken을 서버에 전달하여 앱 토큰(JWT 등)을 발급받습니다.
 */
class KakaoLoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(accessToken: String): Result<LoginResult> =
            authRepository.kakaoLogin(accessToken)
    }

