package com.kuit.afternote.feature.dev.data

import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Release 빌드 전용: local.properties 업데이트 No-op 구현체.
 *
 * 프로덕션 빌드에서는 파일을 수정하지 않습니다.
 */
@Singleton
class ReleaseLocalPropertiesManager @Inject constructor() : LocalPropertiesManager {

    override suspend fun updateTestPassword(newPassword: String): Boolean {
        // Release 빌드에서는 아무 작업도 하지 않음
        return false
    }
}
