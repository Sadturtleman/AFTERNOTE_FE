package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * DELETE /users/me — 회원 탈퇴 UseCase.
 *
 * 로그인한 사용자의 계정을 삭제합니다. 모든 데이터가 영구적으로 삭제되며 복구할 수 없습니다.
 */
class WithdrawAccountUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(): Result<Unit> = userRepository.withdrawAccount()
    }
