package com.kuit.afternote.feature.onboarding.presentation.uimodel

data class AgreementItem(
    val title: String,
    val required: Boolean,
    val description: String? = null,
    val checked: Boolean
)
