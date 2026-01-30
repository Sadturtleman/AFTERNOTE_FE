package com.kuit.afternote.feature.dev.data

import com.kuit.afternote.feature.dev.domain.TestAccountManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Release 빌드 전용: 테스트 계정 비밀번호 관리 No-op 구현체.
 *
 * 프로덕션 빌드에서는 테스트 계정 관리 기능을 사용하지 않으므로 빈 구현을 제공합니다.
 */
@Singleton
class ReleaseTestAccountManager
    @Inject
    constructor() : TestAccountManager {
        override suspend fun getCurrentPassword(): String = ""

        override suspend fun updateStoredPassword(newPassword: String) {
            // Release 빌드에서는 아무 작업도 수행하지 않음
        }
    }
