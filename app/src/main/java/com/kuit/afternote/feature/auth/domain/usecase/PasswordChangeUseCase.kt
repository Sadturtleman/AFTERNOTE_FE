package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 비밀번호 변경 UseCase.
 */
class PasswordChangeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> =
        authRepository.passwordChange(currentPassword, newPassword)
}
