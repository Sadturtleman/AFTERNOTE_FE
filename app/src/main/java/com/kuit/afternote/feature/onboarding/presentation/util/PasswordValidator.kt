package com.kuit.afternote.feature.onboarding.presentation.util

/**
 * 비밀번호 유효성 검증 유틸리티.
 *
 * 비밀번호는 다음 조건을 만족해야 합니다:
 * - 8~20자
 * - 영문 대소문자 포함
 * - 숫자 포함
 * - 특수문자 포함
 */
object PasswordValidator {
    private const val MIN_LENGTH = 8
    private const val MAX_LENGTH = 20

    /**
     * 비밀번호 유효성 검증.
     *
     * @param password 검증할 비밀번호
     * @return 유효성 검증 결과. null이면 유효, 아니면 에러 메시지 반환
     */
    fun validate(password: String): String? {
        if (password.isBlank()) {
            return null // 빈 값은 다른 곳에서 처리
        }

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        val missingRequirements = mutableListOf<String>()
        if (!hasUpperCase || !hasLowerCase) {
            missingRequirements.add("영문 대소문자")
        }
        if (!hasDigit) {
            missingRequirements.add("숫자")
        }
        if (!hasSpecialChar) {
            missingRequirements.add("특수문자")
        }

        return if (password.length < MIN_LENGTH || password.length > MAX_LENGTH || missingRequirements.isNotEmpty()) {
            "비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
        } else {
            null
        }
    }

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 검증.
     *
     * @param password 비밀번호
     * @param passwordConfirm 비밀번호 확인
     * @return 일치 여부
     */
    fun matches(
        password: String,
        passwordConfirm: String
    ): Boolean = password == passwordConfirm
}
