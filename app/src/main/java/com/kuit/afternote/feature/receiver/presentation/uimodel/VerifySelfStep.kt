package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.uimodel.Step

enum class VerifySelfStep(override val value: Int) : Step{
    START(1),
    PHONE_AUTH(1),
    MASTER_CODE(1);

    override fun previous(): VerifySelfStep? =
        when (this) {
            START -> null
            PHONE_AUTH -> START
            MASTER_CODE -> PHONE_AUTH
        }
}
