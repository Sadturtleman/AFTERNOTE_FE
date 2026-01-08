package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.uimodel.Step

enum class VerifyStep(
    override val value: Int
): Step {
    EMAIL_AUTH(1),
    MASTER_KEY_AUTH(2),
    UPLOAD_PDF_AUTH(3),
    END(4);

    override
    fun previous(): VerifyStep? =
        when (this) {
            EMAIL_AUTH -> null
            MASTER_KEY_AUTH -> EMAIL_AUTH
            UPLOAD_PDF_AUTH -> MASTER_KEY_AUTH
            END -> UPLOAD_PDF_AUTH
        }
}
