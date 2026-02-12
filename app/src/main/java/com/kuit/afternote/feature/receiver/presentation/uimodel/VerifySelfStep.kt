package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.uimodel.Step

enum class VerifySelfStep(override val value: Int) : Step {
    START(1),
    EMAIL_AUTH(1),
    EMAIL_CODE(1);

    override fun previous(): VerifySelfStep? =
        when (this) {
            START -> null
            EMAIL_AUTH -> START
            EMAIL_CODE -> EMAIL_AUTH
        }
}
