package com.kuit.afternote.feature.dev.domain

/**
 * 테스트 계정 비밀번호 관리 인터페이스.
 *
 * 개발 모드에서 테스트 계정의 현재 비밀번호를 저장하고 조회하는 데 사용됩니다.
 * "Cycle Password" 전략: 저장된 비밀번호를 사용하여 실제 change-password API를 호출합니다.
 */
interface TestAccountManager {
    /**
     * 저장된 현재 비밀번호를 반환합니다.
     *
     * @return 현재 비밀번호 (저장된 값이 없으면 기본값 반환)
     */
    suspend fun getCurrentPassword(): String

    /**
     * 새로운 비밀번호를 저장합니다.
     *
     * @param newPassword 저장할 새로운 비밀번호
     */
    suspend fun updateStoredPassword(newPassword: String)
}
