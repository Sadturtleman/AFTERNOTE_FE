package com.kuit.afternote.feature.onboarding.presentation.uimodel

enum class SignUpStep(
    val value: Int
) {
    PHONE_AUTH(1),
    IDENTIFY_INPUT(2),
    EMAIL_INPUT(3),
    PW_INPUT(4),

    END(4);

    fun previous(): SignUpStep? =
        when (this) {
            PHONE_AUTH -> null
            IDENTIFY_INPUT -> PHONE_AUTH
            EMAIL_INPUT -> IDENTIFY_INPUT
            PW_INPUT -> EMAIL_INPUT
            END -> PW_INPUT
        }
}
