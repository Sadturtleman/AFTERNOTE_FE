package com.kuit.afternote.feature.setting.presentation.uimodel

sealed class KeyAction {
    data class Number(
        val value: String
    ) : KeyAction()

    object Delete : KeyAction()

    object Confirm : KeyAction()
}
