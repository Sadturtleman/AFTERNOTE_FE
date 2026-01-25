package com.kuit.afternote.feature.setting.presentation.uimodel

sealed class PasswordStep{
    object Setup: PasswordStep()
    object Confirm: PasswordStep()
    object Success: PasswordStep()
}
