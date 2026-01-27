package com.kuit.afternote.feature.dev.domain

/**
 * local.properties 파일의 TEST_PASSWORD를 업데이트하는 인터페이스.
 *
 * Debug 빌드에서만 실제 파일을 업데이트하고, Release 빌드에서는 No-op입니다.
 */
interface LocalPropertiesManager {
    /**
     * local.properties 파일의 TEST_PASSWORD 값을 업데이트합니다.
     *
     * @param newPassword 새로운 비밀번호
     * @return 성공 여부
     */
    suspend fun updateTestPassword(newPassword: String): Boolean
}
