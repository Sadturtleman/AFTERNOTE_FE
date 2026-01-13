package com.kuit.afternote.feature.onboarding.presentation.uimodel

import com.kuit.afternote.core.uimodel.Step

enum class SignUpStep(
    override val value: Int
) : Step {
    PHONE_AUTH(1),
    IDENTIFY_INPUT(2),
    EMAIL_INPUT(3),
    PW_INPUT(4),

    END(4);

    override fun previous(): SignUpStep? =
        when (this) {
            PHONE_AUTH -> null
            IDENTIFY_INPUT -> PHONE_AUTH
            EMAIL_INPUT -> IDENTIFY_INPUT
            PW_INPUT -> EMAIL_INPUT
            END -> PW_INPUT
        }
}
